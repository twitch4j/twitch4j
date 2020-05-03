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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * A helper class that covers a few basic use cases of most library users
 */
@Slf4j
public class TwitchClientHelper implements AutoCloseable {

    /**
     * Holds the channels that are checked for live/offline state changes
     */
    private final Set<EventChannel> listenForGoLive = ConcurrentHashMap.newKeySet();

    /**
     * Holds the channels that are checked for new followers
     */
    private final Set<EventChannel> listenForFollow = ConcurrentHashMap.newKeySet();

    /**
     * TwitchClient
     */
    private final TwitchClient twitchClient;

    /**
     * Event Thread - Stream Status
     */
    protected final Thread streamStatusEventThread;

    /**
     * Event Thread - Followers
     */
    protected final Thread followerEventThread;

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
        this.streamStatusEventThread = new Thread(() -> {
            // check go live / stream events
            if (listenForGoLive.size() > 0) {
                HystrixCommand<StreamList> hystrixGetAllStreams = twitchClient.getHelix().getStreams(defaultAuthToken.getAccessToken(), null, null, listenForGoLive.size(), null, null, null, listenForGoLive.stream().map(EventChannel::getId).collect(Collectors.toList()), null);
                try {
                    List<Stream> streams = hystrixGetAllStreams.execute().getStreams();
                    listenForGoLive.forEach(channel -> {
                        ChannelCache currentChannelCache = channelInformation.getIfPresent(channel.getId());
                        Optional<Stream> stream = streams.stream().filter(s -> s.getUserId().equals(channel.getId())).findFirst();

                        boolean dispatchGoLiveEvent = false;
                        boolean dispatchGoOfflineEvent = false;
                        boolean dispatchTitleChangedEvent = false;
                        boolean dispatchGameChangedEvent = false;

                        if (stream.isPresent() && stream.get().getType().equalsIgnoreCase("live")) {
                            // is live
                            // - live status
                            if (currentChannelCache.getIsLive() != null && currentChannelCache.getIsLive() == false) {
                                dispatchGoLiveEvent = true;
                            }
                            currentChannelCache.setIsLive(true);
                            boolean wasAlreadyLive = dispatchGoLiveEvent != true && currentChannelCache.getIsLive() == true;

                            // - change stream title event
                            if (wasAlreadyLive && currentChannelCache.getTitle() != null && !currentChannelCache.getTitle().equalsIgnoreCase(stream.get().getTitle())) {
                                dispatchTitleChangedEvent = true;
                            }
                            currentChannelCache.setTitle(stream.get().getTitle());

                            // - change game event
                            if (wasAlreadyLive && currentChannelCache.getGameId() != null && !currentChannelCache.getGameId().equals(stream.get().getGameId())) {
                                dispatchGameChangedEvent = true;
                            }
                            currentChannelCache.setGameId(stream.get().getGameId());
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
        });
        this.followerEventThread = new Thread(() -> {
            if(listenForFollow.size() > 0) {
                // check follow events
                for (EventChannel channel : listenForFollow) {
                    HystrixCommand<FollowList> commandGetFollowers = twitchClient.getHelix().getFollowers(defaultAuthToken.getAccessToken(), null, channel.getId(), null, null);
                    try {
                        ChannelCache currentChannelCache = channelInformation.getIfPresent(channel.getId());
                        LocalDateTime lastFollowDate = null;

                        if (currentChannelCache.getLastFollowCheck() != null) {
                            List<Follow> followList = commandGetFollowers.execute().getFollows();
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
        });
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
                if(listenForGoLive.stream().anyMatch(eventChannel -> eventChannel.getName().equalsIgnoreCase(channelName))) {
                    log.info("Channel {} already added for Stream Events", channelName);
                } else {
                    listenForGoLive.add(new EventChannel(user.getId(), user.getLogin()));

                    // initialize cache
                    if (channelInformation.getIfPresent(user.getId()) == null) {
                        channelInformation.put(user.getId(), new ChannelCache(null, null, null, null));
                    }
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
                // add to list
                listenForFollow.remove(new EventChannel(user.getId(), user.getLogin()));

                // invalidate cache
                if (channelInformation.getIfPresent(user.getId()) != null) {
                    channelInformation.invalidate(user.getId());
                }
            });
            // start thread if needed
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
                if(listenForFollow.stream().anyMatch(eventChannel -> eventChannel.getName().equalsIgnoreCase(channelName))) {
                    log.info("Channel {} already added for Follow Events", channelName);
                } else {
                    // add to list
                    listenForFollow.add(new EventChannel(user.getId(), user.getLogin()));

                    // initialize cache
                    if (channelInformation.getIfPresent(user.getId()) == null) {
                        channelInformation.put(user.getId(), new ChannelCache(null, null, null, null));
                    }
                }
            });
            // start thread if needed
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
                listenForFollow.remove(new EventChannel(user.getId(), user.getLogin()));

                // invalidate cache
                if (channelInformation.getIfPresent(user.getId()) != null) {
                    channelInformation.invalidate(user.getId());
                }
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
        if (listenForGoLive.size() > 0) {
            // thread should be active
            executor.scheduleWithFixedDelay(this.streamStatusEventThread, 1, threadRate, TimeUnit.MILLISECONDS);
        } else {
            // thread can be stopped
            executor.remove(this.streamStatusEventThread);
        }

        // follower event thread
        if (listenForFollow.size() > 0) {
            // thread should be active
            executor.scheduleWithFixedDelay(this.followerEventThread, 1, threadRate, TimeUnit.MILLISECONDS);
        } else {
            // thread can be stopped
            executor.remove(this.followerEventThread);
        }
    }

    /**
     * Close
     */
    public void close() {
        executor.remove(this.streamStatusEventThread);
        executor.remove(this.followerEventThread);
    }

}
