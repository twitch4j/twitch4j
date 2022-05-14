package com.github.twitch4j;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.core.domain.Event;
import com.github.twitch4j.chat.events.channel.FollowEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.util.CollectionUtils;
import com.github.twitch4j.common.util.ExponentialBackoffStrategy;
import com.github.twitch4j.common.util.PaginationUtil;
import com.github.twitch4j.domain.ChannelCache;
import com.github.twitch4j.events.ChannelChangeGameEvent;
import com.github.twitch4j.events.ChannelChangeTitleEvent;
import com.github.twitch4j.events.ChannelClipCreatedEvent;
import com.github.twitch4j.events.ChannelFollowCountUpdateEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import com.github.twitch4j.events.ChannelViewerCountUpdateEvent;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.Clip;
import com.github.twitch4j.helix.domain.ClipList;
import com.github.twitch4j.helix.domain.Follow;
import com.github.twitch4j.helix.domain.FollowList;
import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.StreamList;
import com.netflix.hystrix.HystrixCommand;
import lombok.Getter;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * A helper class that covers a few basic use cases of most library users
 */
public class TwitchClientHelper implements IClientHelper {

    static final Logger log = LoggerFactory.getLogger(TwitchClientHelper.class);

    public static final int REQUIRED_THREAD_COUNT = 2;

    /**
     * The greatest number of streams or followers that can be requested in a single API call
     */
    static final int MAX_LIMIT = 100;

    /**
     * Holds the channels that are checked for live/offline state changes
     */
    private final Set<String> listenForGoLive = ConcurrentHashMap.newKeySet();

    /**
     * Holds the channels that are checked for new followers
     */
    private final Set<String> listenForFollow = ConcurrentHashMap.newKeySet();

    /**
     * Holds the channels that are checked for new clip creations
     */
    private final Set<String> listenForClips = ConcurrentHashMap.newKeySet();

    /**
     * Twitch Helix
     */
    @Getter
    private final TwitchHelix twitchHelix;

    /**
     * Event Task - Stream Status
     * <p>
     * Accepts a list of channel ids not exceeding {@link TwitchClientHelper#MAX_LIMIT} in size as the input
     */
    private final Consumer<List<String>> streamStatusEventTask;

    /**
     * The {@link Future} associated with streamStatusEventTask, in an atomic wrapper
     */
    private final AtomicReference<Future<?>> streamStatusEventFuture = new AtomicReference<>();

    /**
     * Event Task - Followers
     * <p>
     * Accepts a channel id as the input; Yields true if the next call should not be delayed
     */
    private final Function<String, Boolean> followerEventTask;

    /**
     * The {@link Future} associated with followerEventTask, in an atomic wrapper
     */
    private final AtomicReference<Future<?>> followerEventFuture = new AtomicReference<>();

    /**
     * Event Task - Clip Creations
     * <p>
     * Accepts a channel id as the input; Yields true if the next call should not be delayed
     */
    private final Function<String, Boolean> clipEventTask;

    /**
     * The {@link Future} associated with clipEventTask, in an atomic wrapper
     */
    private final AtomicReference<Future<?>> clipEventFuture = new AtomicReference<>();

    /**
     * Channel Information Cache
     */
    private final Cache<String, ChannelCache> channelInformation = Caffeine.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .build();

    /**
     * Scheduled Thread Pool Executor
     */
    private final ScheduledThreadPoolExecutor executor;

    /**
     * Holds the {@link ExponentialBackoffStrategy} used for the stream status listener
     */
    private final AtomicReference<ExponentialBackoffStrategy> liveBackoff;

    /**
     * Holds the {@link ExponentialBackoffStrategy} used for the follow listener
     */
    private final AtomicReference<ExponentialBackoffStrategy> followBackoff;

    /**
     * Holds the {@link ExponentialBackoffStrategy} used for the clip creation listener
     */
    private final AtomicReference<ExponentialBackoffStrategy> clipBackoff;

    /**
     * Constructor
     *
     * @param twitchHelix  TwitchHelix
     * @param eventManager EventManager
     * @param executor     ScheduledThreadPoolExecutor
     */
    public TwitchClientHelper(TwitchHelix twitchHelix, EventManager eventManager, ScheduledThreadPoolExecutor executor) {
        this.twitchHelix = twitchHelix;
        this.executor = executor;

        final ExponentialBackoffStrategy defaultBackoff = ExponentialBackoffStrategy.builder().immediateFirst(false).baseMillis(1000L).jitter(false).build();
        this.liveBackoff = new AtomicReference<>(defaultBackoff);
        this.followBackoff = new AtomicReference<>(defaultBackoff.copy());
        this.clipBackoff = new AtomicReference<>(defaultBackoff.copy());

        // Tasks
        this.streamStatusEventTask = channels -> {
            // check go live / stream events
            HystrixCommand<StreamList> hystrixGetAllStreams = twitchHelix.getStreams(null, null, null, channels.size(), null, null, channels, null);
            try {
                Map<String, Stream> streams = new HashMap<>();
                channels.forEach(id -> streams.put(id, null));
                hystrixGetAllStreams.execute().getStreams().forEach(s -> streams.put(s.getUserId(), s));
                liveBackoff.get().reset(); // API call was successful

                streams.forEach((userId, stream) -> {
                    // Check if the channel's live status is still desired to be tracked
                    if (!listenForGoLive.contains(userId))
                        return;

                    ChannelCache currentChannelCache = channelInformation.get(userId, s -> new ChannelCache());
                    // Disabled name updates while Helix returns display name https://github.com/twitchdev/issues/issues/3
                    if (stream != null && currentChannelCache.getUserName() == null)
                        currentChannelCache.setUserName(stream.getUserName());
                    final EventChannel channel = new EventChannel(userId, currentChannelCache.getUserName());

                    boolean dispatchGoLiveEvent = false;
                    boolean dispatchGoOfflineEvent = false;
                    boolean dispatchTitleChangedEvent = false;
                    boolean dispatchGameChangedEvent = false;
                    boolean dispatchViewersChangedEvent = false;

                    if (stream != null && stream.getType().equalsIgnoreCase("live")) {
                        // is live
                        // - live status
                        if (currentChannelCache.getIsLive() != null && !currentChannelCache.getIsLive()) {
                            dispatchGoLiveEvent = true;
                        }
                        currentChannelCache.setIsLive(true);
                        boolean wasAlreadyLive = !dispatchGoLiveEvent && currentChannelCache.getIsLive();

                        // - change stream title event
                        if (wasAlreadyLive && currentChannelCache.getTitle() != null && !currentChannelCache.getTitle().equalsIgnoreCase(stream.getTitle())) {
                            dispatchTitleChangedEvent = true;
                        }
                        currentChannelCache.setTitle(stream.getTitle());

                        // - change game event
                        if (wasAlreadyLive && currentChannelCache.getGameId() != null && !currentChannelCache.getGameId().equals(stream.getGameId())) {
                            dispatchGameChangedEvent = true;
                        }
                        currentChannelCache.setGameId(stream.getGameId());

                        // - change viewer count event
                        if (stream.getViewerCount() != null && !stream.getViewerCount().equals(currentChannelCache.getViewerCount().getAndSet(stream.getViewerCount())) && wasAlreadyLive) {
                            dispatchViewersChangedEvent = true;
                        }
                    } else {
                        // was online previously?
                        if (currentChannelCache.getIsLive() != null && currentChannelCache.getIsLive()) {
                            dispatchGoOfflineEvent = true;
                        }

                        // is offline
                        currentChannelCache.setIsLive(false);
                        currentChannelCache.setTitle(null);
                        currentChannelCache.setGameId(null);
                        currentChannelCache.getViewerCount().lazySet(null);
                    }

                    // dispatch events
                    // - go live event
                    if (dispatchGoLiveEvent) {
                        Event event = new com.github.twitch4j.common.events.channel.ChannelGoLiveEvent(channel, currentChannelCache.getTitle(), currentChannelCache.getGameId());
                        eventManager.publish(event);
                        eventManager.publish(new ChannelGoLiveEvent(channel, stream));
                    }
                    // - go offline event
                    if (dispatchGoOfflineEvent) {
                        Event event = new com.github.twitch4j.common.events.channel.ChannelGoOfflineEvent(channel);
                        eventManager.publish(event);
                        eventManager.publish(new ChannelGoOfflineEvent(channel));
                    }
                    // - title changed event
                    if (dispatchTitleChangedEvent) {
                        Event event = new com.github.twitch4j.common.events.channel.ChannelChangeTitleEvent(channel, currentChannelCache.getTitle());
                        eventManager.publish(event);
                        eventManager.publish(new ChannelChangeTitleEvent(channel, stream));
                    }
                    // - game changed event
                    if (dispatchGameChangedEvent) {
                        Event event = new com.github.twitch4j.common.events.channel.ChannelChangeGameEvent(channel, currentChannelCache.getGameId());
                        eventManager.publish(event);
                        eventManager.publish(new ChannelChangeGameEvent(channel, stream));
                    }
                    // - viewer count changed event
                    if (dispatchViewersChangedEvent) {
                        eventManager.publish(new ChannelViewerCountUpdateEvent(channel, stream));
                    }
                });
            } catch (Exception ex) {
                if (hystrixGetAllStreams != null && hystrixGetAllStreams.isFailedExecution()) {
                    log.trace(hystrixGetAllStreams.getFailedExecutionException().getMessage(), hystrixGetAllStreams.getFailedExecutionException());
                }

                log.error("Failed to check for Stream Events (Live/Offline/...): " + ex.getMessage());
            }
        };
        this.followerEventTask = channelId -> {
            // check follow events
            HystrixCommand<FollowList> commandGetFollowers = twitchHelix.getFollowers(null, null, channelId, null, MAX_LIMIT);
            try {
                ChannelCache currentChannelCache = channelInformation.get(channelId, s -> new ChannelCache());
                Instant lastFollowDate = currentChannelCache.getLastFollowCheck();

                boolean nextRequestCanBeImmediate = false;

                if (lastFollowDate != null) {
                    FollowList executionResult = commandGetFollowers.execute();
                    List<Follow> followList = executionResult.getFollows();
                    followBackoff.get().reset(); // API call was successful

                    // Prepare EventChannel
                    String channelName = currentChannelCache.getUserName(); // Prefer login (even if old) to display_name https://github.com/twitchdev/issues/issues/3#issuecomment-562713594
                    if (channelName == null && !followList.isEmpty()) {
                        channelName = followList.get(0).getToName();
                        currentChannelCache.setUserName(channelName);
                    }
                    EventChannel channel = new EventChannel(channelId, channelName);

                    // Follow Count Event
                    Integer followCount = executionResult.getTotal();
                    Integer oldTotal = currentChannelCache.getFollowers().getAndSet(followCount);
                    if (oldTotal != null && followCount != null && !followCount.equals(oldTotal)) {
                        eventManager.publish(new ChannelFollowCountUpdateEvent(channel, followCount, oldTotal));
                    }

                    // Individual Follow Events
                    for (Follow follow : followList) {
                        // update lastFollowDate
                        if (lastFollowDate == null || follow.getFollowedAtInstant().isAfter(lastFollowDate)) {
                            lastFollowDate = follow.getFollowedAtInstant();
                        }

                        // is new follower?
                        if (follow.getFollowedAtInstant().isAfter(currentChannelCache.getLastFollowCheck())) {
                            // dispatch event
                            FollowEvent event = new FollowEvent(channel, new EventUser(follow.getFromId(), follow.getFromName()));
                            eventManager.publish(event);
                        }
                    }
                } else {
                    nextRequestCanBeImmediate = true; // No API call was made
                }

                if (currentChannelCache.getLastFollowCheck() == null) {
                    // only happens if the user doesn't have any followers at all
                    currentChannelCache.setLastFollowCheck(Instant.now());
                } else if (lastFollowDate != null) {
                    // tracks the date of the latest follow to identify new ones later on
                    currentChannelCache.setLastFollowCheck(lastFollowDate);
                }

                return nextRequestCanBeImmediate;
            } catch (Exception ex) {
                if (commandGetFollowers != null && commandGetFollowers.isFailedExecution()) {
                    log.trace(ex.getMessage(), ex);
                }

                log.error("Failed to check for Follow Events: " + ex.getMessage());
                return false;
            }
        };
        this.clipEventTask = channelId -> {
            // check clip creations
            boolean nextRequestCanBeImmediate = false;

            final ChannelCache currentChannelCache = channelInformation.get(channelId, c -> new ChannelCache());
            final AtomicReference<Instant> windowStart = currentChannelCache.getClipWindowStart();
            final Instant startedAt = windowStart.get();
            final Instant now = Instant.now();

            if (startedAt == null) {
                // initialize clip query window started_at
                nextRequestCanBeImmediate = windowStart.compareAndSet(null, now);
            } else {
                // get all clips in range [startedAt, now]
                final List<Clip> clips = getClips(channelId, startedAt, now);

                // cache channel name if unknown and construct event channel to be passed to events
                if (!clips.isEmpty() && clips.get(0) != null && currentChannelCache.getUserName() == null) {
                    currentChannelCache.setUserName(clips.get(0).getBroadcasterName());
                }
                final EventChannel channel = new EventChannel(channelId, currentChannelCache.getUserName());

                // loop through queried clips
                Instant maxCreatedAt = startedAt;
                for (Clip clip : clips) {
                    if (clip != null && clip.getCreatedAtInstant() != null && clip.getCreatedAtInstant().compareTo(startedAt) > 0) {
                        eventManager.publish(new ChannelClipCreatedEvent(channel, clip)); // found a new clip

                        if (clip.getCreatedAtInstant().compareTo(maxCreatedAt) > 0)
                            maxCreatedAt = clip.getCreatedAtInstant(); // keep track of most recently created clip
                    }
                }

                // next clip window should start just after the most recent clip we've seen for this channel
                final Instant nextStartedAt = maxCreatedAt;
                windowStart.updateAndGet(old -> old == null || old.compareTo(nextStartedAt) < 0 ? nextStartedAt : old);
            }

            return nextRequestCanBeImmediate;
        };
    }

    @Override
    public boolean enableStreamEventListener(String channelId, String channelName) {
        // add to set
        final boolean add = listenForGoLive.add(channelId);
        if (!add) {
            log.info("Channel {} already added for Stream Events", channelName);
        } else {
            // initialize cache
            channelInformation.get(channelId, s -> new ChannelCache(channelName));
        }
        startOrStopEventGenerationThread();
        return add;
    }

    @Override
    public boolean disableStreamEventListenerForId(String channelId) {
        // remove from set
        boolean remove = listenForGoLive.remove(channelId);

        // invalidate cache
        if (!listenForFollow.contains(channelId) && !listenForClips.contains(channelId)) {
            channelInformation.invalidate(channelId);
        } else if (remove) {
            ChannelCache info = channelInformation.getIfPresent(channelId);
            if (info != null) {
                info.setIsLive(null);
                info.setGameId(null);
                info.setTitle(null);
            }
        }

        startOrStopEventGenerationThread();
        return remove;
    }

    @Override
    public boolean enableFollowEventListener(String channelId, String channelName) {
        // add to list
        final boolean add = listenForFollow.add(channelId);
        if (!add) {
            log.info("Channel {} already added for Follow Events", channelName);
        } else {
            // initialize cache
            channelInformation.get(channelId, s -> new ChannelCache(channelName));
        }
        startOrStopEventGenerationThread();
        return add;
    }

    @Override
    public boolean disableFollowEventListenerForId(String channelId) {
        // remove from set
        boolean remove = listenForFollow.remove(channelId);

        // invalidate cache
        if (!listenForGoLive.contains(channelId) && !listenForClips.contains(channelId)) {
            channelInformation.invalidate(channelId);
        } else if (remove) {
            ChannelCache info = channelInformation.getIfPresent(channelId);
            if (info != null) {
                info.setLastFollowCheck(null);
                info.getFollowers().lazySet(null);
            }
        }

        startOrStopEventGenerationThread();
        return remove;
    }

    @Override
    public boolean enableClipEventListener(String channelId, String channelName) {
        // add to set
        final boolean add = listenForClips.add(channelId);
        if (!add) {
            log.info("Channel {} already added for Clip Creation Events", channelName);
        } else {
            // initialize cache
            channelInformation.get(channelId, s -> new ChannelCache(channelName));
        }
        startOrStopEventGenerationThread();
        return add;
    }

    @Override
    public boolean disableClipEventListenerForId(String channelId) {
        // remove from set
        boolean remove = listenForClips.remove(channelId);

        // invalidate cache
        if (!listenForGoLive.contains(channelId) && !listenForFollow.contains(channelId)) {
            channelInformation.invalidate(channelId);
        } else if (remove) {
            ChannelCache info = channelInformation.getIfPresent(channelId);
            if (info != null) {
                info.getClipWindowStart().lazySet(null);
            }
        }

        startOrStopEventGenerationThread();
        return remove;
    }

    @Override
    public void setThreadDelay(long threadDelay) {
        final UnaryOperator<ExponentialBackoffStrategy> updateBackoff = old -> {
            ExponentialBackoffStrategy next = old.toBuilder().baseMillis(threadDelay).build();
            next.setFailures(old.getFailures());
            return next;
        };

        this.liveBackoff.getAndUpdate(updateBackoff);
        this.followBackoff.getAndUpdate(updateBackoff);
        this.clipBackoff.getAndUpdate(updateBackoff);
    }

    @Override
    public Optional<ChannelCache> getCachedInformation(String channelId) {
        return Optional.ofNullable(channelInformation.getIfPresent(channelId));
    }

    @Override
    public void close() {
        final Future<?> streamStatusFuture = this.streamStatusEventFuture.getAndSet(null);
        if (streamStatusFuture != null)
            streamStatusFuture.cancel(false);

        final Future<?> followerFuture = this.followerEventFuture.getAndSet(null);
        if (followerFuture != null)
            followerFuture.cancel(false);

        final Future<?> clipFuture = this.clipEventFuture.getAndSet(null);
        if (clipFuture != null)
            clipFuture.cancel(false);

        listenForGoLive.clear();
        listenForFollow.clear();
        listenForClips.clear();
        channelInformation.invalidateAll();
    }

    /**
     * Start or quit the thread, depending on usage
     */
    private void startOrStopEventGenerationThread() {
        // stream status event thread
        updateListener(listenForGoLive::isEmpty, streamStatusEventFuture, this::runRecursiveStreamStatusCheck, liveBackoff);

        // follower event thread
        updateListener(listenForFollow::isEmpty, followerEventFuture, this::runRecursiveFollowerCheck, followBackoff);

        // clip creation event thread
        updateListener(listenForClips::isEmpty, clipEventFuture, this::runRecursiveClipCheck, clipBackoff);
    }

    /**
     * Performs the "heavy lifting" of starting or stopping a listener
     *
     * @param stopCondition   yields whether the listener should be running
     * @param futureReference the current listener in an atomic wrapper
     * @param startCommand    the command to start the listener
     * @param backoff         the {@link ExponentialBackoffStrategy} for the listener
     */
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter") // Acceptable as futureReference is only streamStatusEventFuture or followerEventFuture
    private void updateListener(BooleanSupplier stopCondition, AtomicReference<Future<?>> futureReference, Runnable startCommand, AtomicReference<ExponentialBackoffStrategy> backoff) {
        if (stopCondition.getAsBoolean()) {
            // Optimization to avoid obtaining an unnecessary lock
            if (futureReference.get() != null) {
                Future<?> future = null;
                synchronized (futureReference) {
                    if (stopCondition.getAsBoolean()) // Ensure conditions haven't changed in the time it took to acquire this lock
                        future = futureReference.getAndSet(null); // Clear out the listener future
                }

                // Cancel the future
                if (future != null) {
                    future.cancel(false);
                    backoff.get().reset(); // Ideally we would decrement to zero over time rather than instantly resetting
                }
            }
        } else {
            // Optimization to avoid obtaining an unnecessary lock
            if (futureReference.get() == null) {
                // Must synchronize to prevent race condition where multiple threads could be created
                synchronized (futureReference) {
                    // Start if not already started
                    if (!stopCondition.getAsBoolean() && futureReference.get() == null)
                        futureReference.set(executor.schedule(startCommand, backoff.get().get(), TimeUnit.MILLISECONDS));
                }
            }
        }
    }

    /**
     * Initiates the stream status listener execution
     */
    private void runRecursiveStreamStatusCheck() {
        runRecursiveCheck(streamStatusEventFuture, executor, CollectionUtils.chunked(listenForGoLive, MAX_LIMIT), liveBackoff, this::runRecursiveStreamStatusCheck, chunk -> {
            streamStatusEventTask.accept(chunk);
            return false; // treat as always consuming from the api rate-limit
        });
    }

    /**
     * Initiates the follower listener execution
     */
    private void runRecursiveFollowerCheck() {
        runRecursiveCheck(followerEventFuture, executor, new ArrayList<>(listenForFollow), followBackoff, this::runRecursiveFollowerCheck, followerEventTask);
    }

    /**
     * Initiates the clip creation listener execution
     */
    private void runRecursiveClipCheck() {
        runRecursiveCheck(clipEventFuture, executor, new ArrayList<>(listenForClips), clipBackoff, this::runRecursiveClipCheck, clipEventTask);
    }

    private List<Clip> getClips(String channelId, Instant startedAt, Instant endedAt) {
        return PaginationUtil.getPaginated(
            cursor -> {
                final HystrixCommand<ClipList> commandGetClips = twitchHelix.getClips(null, channelId, null, null, cursor, null, MAX_LIMIT, startedAt, endedAt);
                try {
                    ClipList result = commandGetClips.execute();
                    clipBackoff.get().reset(); // successful api call
                    return result;
                } catch (Exception ex) {
                    if (commandGetClips != null && commandGetClips.isFailedExecution()) {
                        log.trace(ex.getMessage(), ex);
                    }
                    log.error("Failed to check for Clip Events: " + ex.getMessage());
                    clipBackoff.get().get(); // increment failures
                    return null;
                }
            },
            ClipList::getData,
            call -> call.getPagination() != null ? call.getPagination().getCursor() : null,
            20
        );
    }

    @Value
    private static class ListenerRunnable<T> implements Runnable {
        ScheduledExecutorService executor;
        List<T> channels;
        AtomicReference<Future<?>> futureReference;
        AtomicReference<ExponentialBackoffStrategy> backoff;
        Runnable startCommand;
        Function<T, Boolean> executeSingle;

        @Override
        public void run() {
            if (channels.isEmpty()) {
                // Try again later if the task wasn't cancelled
                if (futureReference.get() != null)
                    synchronized (futureReference) {
                        if (cancel(futureReference)) {
                            backoff.get().reset();
                            futureReference.set(executor.schedule(startCommand, backoff.get().get(), TimeUnit.MILLISECONDS));
                        }
                    }
            } else {
                // Start execution from the first element
                run(0);
            }
        }

        private void run(final int index) {
            // If no api call was made by executeSingle, it will return true. Then, we do not need to add any delay before checking the next channel.
            Boolean skipDelay = executeSingle.apply(channels.get(index));

            // Queue up the next check (if the task hasn't been cancelled)
            if (futureReference.get() != null)
                synchronized (futureReference) {
                    if (cancel(futureReference))
                        futureReference.set(
                            executor.schedule(
                                index + 1 < channels.size() ? () -> run(index + 1) : startCommand,
                                skipDelay ? 0 : backoff.get().get(),
                                TimeUnit.MILLISECONDS
                            )
                        );
                }
        }
    }

    private static boolean cancel(AtomicReference<Future<?>> futureRef) {
        Future<?> future = futureRef.get();
        return future != null && future.cancel(false);
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private static <T> void runRecursiveCheck(AtomicReference<Future<?>> future, ScheduledExecutorService executor, List<T> units, AtomicReference<ExponentialBackoffStrategy> backoff, Runnable startCommand, Function<T, Boolean> task) {
        if (future.get() != null)
            synchronized (future) {
                if (cancel(future))
                    future.set(
                        executor.submit(
                            new ListenerRunnable<>(
                                executor,
                                units,
                                future,
                                backoff,
                                startCommand,
                                task
                            )
                        )
                    );
            }
    }

}
