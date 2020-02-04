package com.github.twitch4j;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
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
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private List<EventChannel> listenForGoLive = new ArrayList<>();

    /**
     * Holds the channels that are checked for new followers
     */
    private List<EventChannel> listenForFollow = new ArrayList<>();

    /**
     * TwitchClient
     */
    private TwitchClient twitchClient;

    /**
     * Event Listener Thread
     */
    protected final Thread eventGenerationThread;

    /**
     * Event Listener Thread
     */
    protected Boolean stopEventGenerationThread = false;

    /**
     * Channel Information Cache
     */
    private Cache<String, ChannelCache> channelInformation = Caffeine.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .maximumSize(10_000)
        .build();

    /**
     * Constructor
     *
     * @param twitchClient TwitchClient
     */
    public TwitchClientHelper(TwitchClient twitchClient) {
        this.twitchClient = twitchClient;

        // Thread
        this.eventGenerationThread = new Thread(() -> {
            log.debug("Started TwitchClientHelper Thread to listen for goLive/Follow events");

            while (stopEventGenerationThread == false) {
                try {
                    // check if the thread was interrupted
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }

                    // check go live / stream events
                    if (listenForGoLive.size() > 0) {
                        HystrixCommand<StreamList> hystrixGetAllStreams = twitchClient.getHelix().getStreams(null, null, null, listenForGoLive.size(), null, null, null, listenForGoLive.stream().map(EventChannel::getId).collect(Collectors.toList()), null);
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

                    // check follow events
                    for (EventChannel channel : listenForFollow) {
                        HystrixCommand<FollowList> commandGetFollowers = twitchClient.getHelix().getFollowers(null, null, channel.getId(), null, null);

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

                    // sleep one second
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    // exit thread, since it's not needed anymore
                    log.debug("TwitchClientHelper Thread has been disabled, it's not needed anymore since we aren't listening for any events with the helper.");
                    return;
                } catch (Exception ex) {
                    log.error("Failed to check for events in TwitchClientHelper Thread: " + ex.getMessage());
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
        UserList users = twitchClient.getHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> {
                // add to list
                listenForGoLive.add(new EventChannel(user.getId(), user.getLogin()));

                // initialize cache
                if (channelInformation.getIfPresent(user.getId()) == null) {
                    channelInformation.put(user.getId(), new ChannelCache(null, null, null, null));
                }

                // start thread if needed
                startOrStopEventGenerationThread();
            });
        } else {
            log.error("Failed to add channel " + channelName + " to stream event listener!");
        }
    }

    /**
     * Disable StreamEvent Listener
     *
     * @param channelName Channel Name
     */
    public void disableStreamEventListener(String channelName) {
        UserList users = twitchClient.getHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> {
                // add to list
                listenForFollow.remove(new EventChannel(user.getId(), user.getLogin()));

                // invalidate cache
                if (channelInformation.getIfPresent(user.getId()) != null) {
                    channelInformation.invalidate(user.getId());
                }

                // start thread if needed
                startOrStopEventGenerationThread();
            });
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
        UserList users = twitchClient.getHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> {
                // add to list
                listenForFollow.add(new EventChannel(user.getId(), user.getLogin()));

                // initialize cache
                if (channelInformation.getIfPresent(user.getId()) == null) {
                    channelInformation.put(user.getId(), new ChannelCache(null, null, null, null));
                }

                // start thread if needed
                startOrStopEventGenerationThread();
            });
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
        UserList users = twitchClient.getHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> {
                // add to list
                listenForFollow.remove(new EventChannel(user.getId(), user.getLogin()));

                // invalidate cache
                if (channelInformation.getIfPresent(user.getId()) != null) {
                    channelInformation.invalidate(user.getId());
                }

                // start thread if needed
                startOrStopEventGenerationThread();
            });
        } else {
            log.error("Failed to remove channel " + channelName + " from follow listener!");
        }
    }

    /**
     * Start or quit the thread, depending on usage
     */
    private void startOrStopEventGenerationThread() {
        if (listenForGoLive.size() > 0 || listenForFollow.size() > 0) {
            // thread should be active
            if (!eventGenerationThread.isAlive()) {
                eventGenerationThread.start();
            }
        } else {
            // thread can be stopped
            if (eventGenerationThread.isAlive() && !eventGenerationThread.isInterrupted()) {
                eventGenerationThread.interrupt();
            }
        }
    }

    /**
     * Close
     */
    public void close() {
        stopEventGenerationThread = true;
        eventGenerationThread.interrupt();
    }

}
