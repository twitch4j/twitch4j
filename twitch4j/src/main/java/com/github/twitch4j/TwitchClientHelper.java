package com.github.twitch4j;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.domain.Event;
import com.github.twitch4j.chat.events.channel.FollowEvent;
import com.github.twitch4j.common.events.channel.ChannelChangeGameEvent;
import com.github.twitch4j.common.events.channel.ChannelChangeTitleEvent;
import com.github.twitch4j.common.events.channel.ChannelGoLiveEvent;
import com.github.twitch4j.common.events.channel.ChannelGoOfflineEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.domain.ChannelCache;
import com.github.twitch4j.helix.domain.*;
import com.netflix.hystrix.HystrixCommand;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
     * Default Auth Token for Twitch API Requests
     */
    @Setter
    private OAuth2Credential defaultAuthToken = null;

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
     * @param executor ScheduledThreadPoolExecutor
     */
    public TwitchClientHelper(TwitchClient twitchClient, ScheduledThreadPoolExecutor executor) {
        this.twitchClient = twitchClient;
        this.executor = executor;
        // Threads
        this.streamStatusEventTask = () -> {
            // check go live / stream events
            if (listenForGoLive.size() > 0) {
                HystrixCommand<StreamList> hystrixGetAllStreams = twitchClient.getHelix().getStreams(defaultAuthToken.getAccessToken(), null, null, listenForGoLive.size(), null, null, null, new ArrayList<>(listenForGoLive), null);
                try {
                    Map<String, Stream> streams = new HashMap<>();
                    listenForGoLive.forEach(id -> streams.put(id, null));
                    hystrixGetAllStreams.execute().getStreams().forEach(s -> streams.put(s.getUserId(), s));

                    streams.forEach((userId, stream) -> {
                        // Check if the channel's live status is still desired to be tracked
                        if (!listenForGoLive.contains(userId))
                            return;

                        ChannelCache currentChannelCache = channelInformation.get(userId, s -> new ChannelCache(null, null, null, null, null));
                        if (stream != null)
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
                            Event event = new ChannelGoLiveEvent(channel, currentChannelCache.getTitle(), currentChannelCache.getGameId());
                            twitchClient.getEventManager().publish(event);
                        }
                        // - go offline event
                        if (dispatchGoOfflineEvent) {
                            Event event = new ChannelGoOfflineEvent(channel);
                            twitchClient.getEventManager().publish(event);
                        }
                        // - title changed event
                        if (dispatchTitleChangedEvent) {
                            Event event = new ChannelChangeTitleEvent(channel, currentChannelCache.getTitle());
                            twitchClient.getEventManager().publish(event);
                        }
                        // - game changed event
                        if (dispatchGameChangedEvent) {
                            Event event = new ChannelChangeGameEvent(channel, currentChannelCache.getGameId());
                            twitchClient.getEventManager().publish(event);
                        }
                    });
                } catch (Exception ex) {
                    if (hystrixGetAllStreams != null && hystrixGetAllStreams.isFailedExecution()) {
                        log.trace(hystrixGetAllStreams.getFailedExecutionException().getMessage(), hystrixGetAllStreams.getFailedExecutionException());
                    }

                    log.error("Failed to check for Stream Events (Live/Offline/...): " + ex.getMessage());
                }
            }
        };
        this.followerEventTask = () -> {
            if (listenForFollow.size() > 0) {
                // check follow events
                for (String channelId : listenForFollow) {
                    HystrixCommand<FollowList> commandGetFollowers = twitchClient.getHelix().getFollowers(defaultAuthToken.getAccessToken(), null, channelId, null, null);
                    try {
                        ChannelCache currentChannelCache = channelInformation.get(channelId, s -> new ChannelCache(null, null, null, null, null));
                        LocalDateTime lastFollowDate = null;

                        if (currentChannelCache.getLastFollowCheck() != null) {
                            List<Follow> followList = commandGetFollowers.execute().getFollows();
                            EventChannel channel = null;
                            if (!followList.isEmpty())
                                channel = new EventChannel(channelId, followList.get(0).getToName());
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
            }
        };
    }

    /**
     * Enable StreamEvent Listener
     *
     * @param channelName Channel Name
     */
    public void enableStreamEventListener(String channelName) {
        UserList users = twitchClient.getHelix().getUsers(defaultAuthToken.getAccessToken(), null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> {
                // add to list
                final boolean add = listenForGoLive.add(user.getId());
                if (!add) {
                    log.info("Channel {} already added for Stream Events", channelName);
                } else {
                    // initialize cache
                    channelInformation.get(user.getId(), s -> new ChannelCache(user.getLogin(), null, null, null, null));
                }
            });
            startOrStopEventGenerationThread();
        } else {
            log.error("Failed to add channel {} to stream event listener!", channelName);
        }
    }

    /**
     * Disable StreamEvent Listener
     *
     * @param channelName Channel Name
     */
    public void disableStreamEventListener(String channelName) {
        UserList users = twitchClient.getHelix().getUsers(defaultAuthToken.getAccessToken(), null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> {
                // remove from list
                listenForGoLive.remove(user.getId());

                // invalidate cache
                channelInformation.invalidate(user.getId());
            });
            startOrStopEventGenerationThread();
        } else {
            log.error("Failed to remove channel " + channelName + " from stream event listener!");
        }
    }

    /**
     * Follow Listener
     *
     * @param channelName Channel Name
     */
    public void enableFollowEventListener(String channelName) {
        UserList users = twitchClient.getHelix().getUsers(defaultAuthToken.getAccessToken(), null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> {
                // add to list
                final boolean add = listenForFollow.add(user.getId());
                if (!add) {
                    log.info("Channel {} already added for Follow Events", channelName);
                } else {
                    // initialize cache
                    channelInformation.get(user.getId(), s -> new ChannelCache(user.getLogin(), null, null, null, null));
                }
            });
            startOrStopEventGenerationThread();
        } else {
            log.error("Failed to add channel " + channelName + " to Follow Listener, maybe it doesn't exist!");
        }
    }

    /**
     * Disable Follow Listener
     *
     * @param channelName Channel Name
     */
    public void disableFollowEventListener(String channelName) {
        UserList users = twitchClient.getHelix().getUsers(defaultAuthToken.getAccessToken(), null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> {
                // add to list
                listenForFollow.remove(user.getId());

                // invalidate cache
                channelInformation.invalidate(user.getId());
            });
            startOrStopEventGenerationThread();
        } else {
            log.error("Failed to remove channel " + channelName + " from follow listener!");
        }
    }

    /**
     * Start or quit the thread, depending on usage
     */
    private void startOrStopEventGenerationThread() {
        // stream status event thread
        streamStatusEventFuture.updateAndGet(scheduledFuture -> {
            if (listenForGoLive.size() > 0) {
                if (scheduledFuture == null)
                    return executor.scheduleAtFixedRate(this.streamStatusEventTask, 1, threadRate,
                        TimeUnit.MILLISECONDS);
                return scheduledFuture;
            } else {
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(false);
                }
                return null;
            }
        });

        // follower event thread
        followerEventFuture.updateAndGet(scheduledFuture -> {
            if (listenForFollow.size() > 0) {
                if (scheduledFuture == null)
                    return executor.scheduleAtFixedRate(this.followerEventTask, 1, threadRate, TimeUnit.MILLISECONDS);
                return scheduledFuture;
            } else {
                if (scheduledFuture != null)
                    scheduledFuture.cancel(false);
                return null;
            }
        });
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
    }

}
