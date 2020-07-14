package com.github.twitch4j;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.philippheuer.events4j.core.domain.Event;
import com.github.twitch4j.chat.events.channel.FollowEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.util.CollectionUtils;
import com.github.twitch4j.domain.ChannelCache;
import com.github.twitch4j.events.ChannelChangeGameEvent;
import com.github.twitch4j.events.ChannelChangeTitleEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import com.github.twitch4j.helix.domain.*;
import com.netflix.hystrix.HystrixCommand;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A helper class that covers a few basic use cases of most library users
 */
@Slf4j
public class TwitchClientHelper implements AutoCloseable {

    public static final int REQUIRED_THREAD_COUNT = 2;

    /**
     * The greatest number of streams or followers that can be requested in a single API call
     */
    private static final int MAX_LIMIT = 100;

    /**
     * Holds the channels that are checked for live/offline state changes
     */
    private final Set<String> listenForGoLive = ConcurrentHashMap.newKeySet();

    /**
     * Holds the channels that are checked for new followers
     */
    private final Set<String> listenForFollow = ConcurrentHashMap.newKeySet();

    /**
     * TwitchClient
     */
    private final TwitchClient twitchClient;

    /**
     * Event Task - Stream Status
     */
    private final Runnable streamStatusEventTask;

    /**
     * The {@link ScheduledFuture} associated with streamStatusEventTask, in an atomic wrapper
     */
    private final AtomicReference<ScheduledFuture<?>> streamStatusEventFuture = new AtomicReference<>();

    /**
     * Event Task - Followers
     */
    private final Runnable followerEventTask;

    /**
     * The {@link ScheduledFuture} associated with followerEventTask, in an atomic wrapper
     */
    private final AtomicReference<ScheduledFuture<?>> followerEventFuture = new AtomicReference<>();

    /**
     * Channel Information Cache
     */
    private final Cache<String, ChannelCache> channelInformation = Caffeine.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .maximumSize(10_000)
        .build();

    /**
     * Scheduled Thread Pool Executor
     */
    private final ScheduledThreadPoolExecutor executor;

    /**
     * Thread Rate
     */
    @Setter
    private long threadRate;

    /**
     * Constructor
     *
     * @param twitchClient TwitchClient
     * @param executor     ScheduledThreadPoolExecutor
     */
    public TwitchClientHelper(TwitchClient twitchClient, ScheduledThreadPoolExecutor executor) {
        this.twitchClient = twitchClient;
        this.executor = executor;
        // Threads
        this.streamStatusEventTask = () -> {
            // check go live / stream events
            CollectionUtils.chunked(listenForGoLive, MAX_LIMIT).forEach(channels -> {
                HystrixCommand<StreamList> hystrixGetAllStreams = twitchClient.getHelix().getStreams(null, null, null, channels.size(), null, null, null, channels, null);
                try {
                    Map<String, Stream> streams = new HashMap<>();
                    channels.forEach(id -> streams.put(id, null));
                    hystrixGetAllStreams.execute().getStreams().forEach(s -> streams.put(s.getUserId(), s));

                    streams.forEach((userId, stream) -> {
                        // Check if the channel's live status is still desired to be tracked
                        if (!listenForGoLive.contains(userId))
                            return;

                        ChannelCache currentChannelCache = channelInformation.get(userId, s -> new ChannelCache(null, null, null, null, null));
                        // Disabled name updates while Helix returns display name https://github.com/twitchdev/issues/issues/3
                        if (stream != null && currentChannelCache.getUserName() == null)
                            currentChannelCache.setUserName(stream.getUserName());
                        final EventChannel channel = new EventChannel(userId, currentChannelCache.getUserName());

                        boolean dispatchGoLiveEvent = false;
                        boolean dispatchGoOfflineEvent = false;
                        boolean dispatchTitleChangedEvent = false;
                        boolean dispatchGameChangedEvent = false;

                        if (stream != null && stream.getType().equalsIgnoreCase("live")) {
                            // is live
                            // - live status
                            if (currentChannelCache.getIsLive() != null && currentChannelCache.getIsLive() == false) {
                                dispatchGoLiveEvent = true;
                            }
                            currentChannelCache.setIsLive(true);
                            boolean wasAlreadyLive = dispatchGoLiveEvent != true && currentChannelCache.getIsLive() == true;

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
                        } else {
                            // was online previously?
                            if (currentChannelCache.getIsLive() != null && currentChannelCache.getIsLive() == true) {
                                dispatchGoOfflineEvent = true;
                            }

                            // is offline
                            currentChannelCache.setIsLive(false);
                            currentChannelCache.setTitle(null);
                            currentChannelCache.setGameId(null);
                        }

                        // dispatch events
                        // - go live event
                        if (dispatchGoLiveEvent) {
                            Event event = new com.github.twitch4j.common.events.channel.ChannelGoLiveEvent(channel, currentChannelCache.getTitle(), currentChannelCache.getGameId());
                            twitchClient.getEventManager().publish(event);
                            twitchClient.getEventManager().publish(new ChannelGoLiveEvent(channel, stream));
                        }
                        // - go offline event
                        if (dispatchGoOfflineEvent) {
                            Event event = new com.github.twitch4j.common.events.channel.ChannelGoOfflineEvent(channel);
                            twitchClient.getEventManager().publish(event);
                            twitchClient.getEventManager().publish(new ChannelGoOfflineEvent(channel));
                        }
                        // - title changed event
                        if (dispatchTitleChangedEvent) {
                            Event event = new com.github.twitch4j.common.events.channel.ChannelChangeTitleEvent(channel, currentChannelCache.getTitle());
                            twitchClient.getEventManager().publish(event);
                            twitchClient.getEventManager().publish(new ChannelChangeTitleEvent(channel, stream));
                        }
                        // - game changed event
                        if (dispatchGameChangedEvent) {
                            Event event = new com.github.twitch4j.common.events.channel.ChannelChangeGameEvent(channel, currentChannelCache.getGameId());
                            twitchClient.getEventManager().publish(event);
                            twitchClient.getEventManager().publish(new ChannelChangeGameEvent(channel, stream));
                        }
                    });
                } catch (Exception ex) {
                    if (hystrixGetAllStreams != null && hystrixGetAllStreams.isFailedExecution()) {
                        log.trace(hystrixGetAllStreams.getFailedExecutionException().getMessage(), hystrixGetAllStreams.getFailedExecutionException());
                    }

                    log.error("Failed to check for Stream Events (Live/Offline/...): " + ex.getMessage());
                }
            });
        };
        this.followerEventTask = () -> {
            // check follow events
            for (String channelId : listenForFollow) {
                HystrixCommand<FollowList> commandGetFollowers = twitchClient.getHelix().getFollowers(null, null, channelId, null, MAX_LIMIT);
                try {
                    ChannelCache currentChannelCache = channelInformation.get(channelId, s -> new ChannelCache(null, null, null, null, null));
                    LocalDateTime lastFollowDate = null;

                    if (currentChannelCache.getLastFollowCheck() != null) {
                        List<Follow> followList = commandGetFollowers.execute().getFollows();
                        EventChannel channel = null;
                        if (!followList.isEmpty()) {
                            // Prefer login (even if old) to display_name https://github.com/twitchdev/issues/issues/3#issuecomment-562713594
                            if (currentChannelCache.getUserName() == null)
                                currentChannelCache.setUserName(followList.get(0).getToName());
                            channel = new EventChannel(channelId, currentChannelCache.getUserName());
                        }
                        for (Follow follow : followList) {
                            // update lastFollowDate
                            if (lastFollowDate == null || follow.getFollowedAt().compareTo(lastFollowDate) > 0) {
                                lastFollowDate = follow.getFollowedAt();
                            }

                            // is new follower?
                            if (follow.getFollowedAt().compareTo(currentChannelCache.getLastFollowCheck()) > 0) {
                                // dispatch event
                                FollowEvent event = new FollowEvent(channel, new EventUser(follow.getFromId(), follow.getFromName()));
                                twitchClient.getEventManager().publish(event);
                            }
                        }
                    }

                    if (currentChannelCache.getLastFollowCheck() == null) {
                        // only happens if the user doesn't have any followers at all
                        currentChannelCache.setLastFollowCheck(LocalDateTime.now(ZoneId.of("UTC")));
                    } else {
                        // tracks the date of the latest follow to identify new ones later on
                        currentChannelCache.setLastFollowCheck(lastFollowDate);
                    }
                } catch (Exception ex) {
                    if (commandGetFollowers != null && commandGetFollowers.isFailedExecution()) {
                        log.trace(ex.getMessage(), ex);
                    }

                    log.error("Failed to check for Follow Events: " + ex.getMessage());
                }
            }
        };
    }

    /**
     * Enable StreamEvent Listener
     *
     * @param channelName Channel Name
     */
    public void enableStreamEventListener(String channelName) {
        UserList users = twitchClient.getHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> enableStreamEventListener(user.getId(), user.getLogin()));
        } else {
            log.error("Failed to add channel {} to stream event listener!", channelName);
        }
    }

    /**
     * Enable StreamEvent Listener for the given channel names
     *
     * @param channelNames the channel names to be added
     */
    public void enableStreamEventListener(Iterable<String> channelNames) {
        CollectionUtils.chunked(channelNames, MAX_LIMIT).forEach(channels -> {
            UserList users = twitchClient.getHelix().getUsers(null, null, channels).execute();
            users.getUsers().forEach(user -> enableStreamEventListener(user.getId(), user.getLogin()));
        });
    }

    /**
     * Enable StreamEvent Listener, without invoking a Helix API call
     *
     * @param channelId   Channel Id
     * @param channelName Channel Name
     * @return true if the channel was added, false otherwise
     */
    public boolean enableStreamEventListener(String channelId, String channelName) {
        // add to set
        final boolean add = listenForGoLive.add(channelId);
        if (!add) {
            log.info("Channel {} already added for Stream Events", channelName);
        } else {
            // initialize cache
            channelInformation.get(channelId, s -> new ChannelCache(channelName, null, null, null, null));
        }
        startOrStopEventGenerationThread();
        return add;
    }

    /**
     * Disable StreamEvent Listener
     *
     * @param channelName Channel Name
     */
    public void disableStreamEventListener(String channelName) {
        UserList users = twitchClient.getHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> disableStreamEventListenerForId(user.getId()));
        } else {
            log.error("Failed to remove channel " + channelName + " from stream event listener!");
        }
    }

    /**
     * Disable StreamEvent Listener for the given channel names
     *
     * @param channelNames the channel names to be removed
     */
    public void disableStreamEventListener(Iterable<String> channelNames) {
        CollectionUtils.chunked(channelNames, MAX_LIMIT).forEach(channels -> {
            UserList users = twitchClient.getHelix().getUsers(null, null, channels).execute();
            users.getUsers().forEach(user -> disableStreamEventListenerForId(user.getId()));
        });
    }

    /**
     * Disable StreamEventListener, without invoking a Helix API call
     *
     * @param channelId Channel Id
     * @return true if the channel was removed, false otherwise
     */
    public boolean disableStreamEventListenerForId(String channelId) {
        // remove from set
        boolean remove = listenForGoLive.remove(channelId);

        // invalidate cache
        channelInformation.invalidate(channelId);

        startOrStopEventGenerationThread();
        return remove;
    }

    /**
     * Follow Listener
     *
     * @param channelName Channel Name
     */
    public void enableFollowEventListener(String channelName) {
        UserList users = twitchClient.getHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> enableFollowEventListener(user.getId(), user.getLogin()));
        } else {
            log.error("Failed to add channel " + channelName + " to Follow Listener, maybe it doesn't exist!");
        }
    }

    /**
     * Enable Follow Listener for the given channel names
     *
     * @param channelNames the channel names to be added
     */
    public void enableFollowEventListener(Iterable<String> channelNames) {
        CollectionUtils.chunked(channelNames, MAX_LIMIT).forEach(channels -> {
            UserList users = twitchClient.getHelix().getUsers(null, null, channels).execute();
            users.getUsers().forEach(user -> enableFollowEventListener(user.getId(), user.getLogin()));
        });
    }

    /**
     * Enable Follow Listener, without invoking a Helix API call
     *
     * @param channelId   Channel Id
     * @param channelName Channel Name
     * @return true if the channel was added, false otherwise
     */
    public boolean enableFollowEventListener(String channelId, String channelName) {
        // add to list
        final boolean add = listenForFollow.add(channelId);
        if (!add) {
            log.info("Channel {} already added for Follow Events", channelName);
        } else {
            // initialize cache
            channelInformation.get(channelId, s -> new ChannelCache(channelName, null, null, null, null));
        }
        startOrStopEventGenerationThread();
        return add;
    }

    /**
     * Disable Follow Listener
     *
     * @param channelName Channel Name
     */
    public void disableFollowEventListener(String channelName) {
        UserList users = twitchClient.getHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> disableFollowEventListenerForId(user.getId()));
        } else {
            log.error("Failed to remove channel " + channelName + " from follow listener!");
        }
    }

    /**
     * Disable Follow Listener for the given channel names
     *
     * @param channelNames the channel names to be removed
     */
    public void disableFollowEventListener(Iterable<String> channelNames) {
        CollectionUtils.chunked(channelNames, MAX_LIMIT).forEach(channels -> {
            UserList users = twitchClient.getHelix().getUsers(null, null, channels).execute();
            users.getUsers().forEach(user -> disableFollowEventListenerForId(user.getId()));
        });
    }

    /**
     * Disable Follow Listener, without invoking a Helix API call
     *
     * @param channelId Channel Id
     * @return true when a previously-tracked channel was removed, false otherwise
     */
    public boolean disableFollowEventListenerForId(String channelId) {
        // remove from set
        boolean remove = listenForFollow.remove(channelId);

        // invalidate cache
        channelInformation.invalidate(channelId);

        startOrStopEventGenerationThread();
        return remove;
    }

    /**
     * Start or quit the thread, depending on usage
     */
    @Synchronized
    private void startOrStopEventGenerationThread() {
        // stream status event thread
        if (listenForGoLive.size() > 0) {
            if (streamStatusEventFuture.get() == null)
                streamStatusEventFuture.set(executor.scheduleAtFixedRate(this.streamStatusEventTask, 1, threadRate, TimeUnit.MILLISECONDS));
        } else {
            final ScheduledFuture<?> scheduledFuture = streamStatusEventFuture.getAndSet(null);
            if (scheduledFuture != null)
                scheduledFuture.cancel(false);
        }

        // follower event thread
        if (listenForFollow.size() > 0) {
            if (followerEventFuture.get() == null)
                followerEventFuture.set(executor.scheduleAtFixedRate(this.followerEventTask, 1, threadRate, TimeUnit.MILLISECONDS));
        } else {
            final ScheduledFuture<?> scheduledFuture = followerEventFuture.getAndSet(null);
            if (scheduledFuture != null)
                scheduledFuture.cancel(false);
        }
    }

    /**
     * Close
     */
    public void close() {
        final ScheduledFuture<?> streamStatusFuture = this.streamStatusEventFuture.get();
        if (streamStatusFuture != null)
            streamStatusFuture.cancel(false);

        final ScheduledFuture<?> followerFuture = this.followerEventFuture.get();
        if (followerFuture != null)
            followerFuture.cancel(false);

        streamStatusEventFuture.lazySet(null);
        followerEventFuture.lazySet(null);
    }

}
