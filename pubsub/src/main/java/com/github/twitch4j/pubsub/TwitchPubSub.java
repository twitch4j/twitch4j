package com.github.twitch4j.pubsub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.common.util.ExponentialBackoffStrategy;
import com.github.twitch4j.common.util.TimeUtils;
import com.github.twitch4j.common.util.TwitchUtils;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.*;
import com.github.twitch4j.pubsub.enums.PubSubType;
import com.github.twitch4j.pubsub.enums.TMIConnectionState;
import com.github.twitch4j.pubsub.events.*;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Twitch PubSub
 */
@Slf4j
public class TwitchPubSub implements AutoCloseable {

    public static final int REQUIRED_THREAD_COUNT = 1;

    /**
     * EventManager
     */
    @Getter
    private final EventManager eventManager;

    /**
     * The WebSocket Server
     */
    private static final String WEB_SOCKET_SERVER = "wss://pubsub-edge.twitch.tv:443";

    /**
     * WebSocket Client
     */
    @Setter(AccessLevel.NONE)
    private volatile WebSocket webSocket;

    /**
     * The connection state
     * Default: ({@link TMIConnectionState#DISCONNECTED})
     */
    private volatile TMIConnectionState connectionState = TMIConnectionState.DISCONNECTED;

    /**
     * Whether {@link #flushCommand} is currently executing
     */
    private final AtomicBoolean flushing = new AtomicBoolean();

    /**
     * Whether an expedited flush has already been submitted
     */
    private final AtomicBoolean flushRequested = new AtomicBoolean();

    /**
     * The {@link Runnable} for flushing the {@link #commandQueue}
     */
    private final Runnable flushCommand;

    /**
     * Command Queue Thread
     */
    protected final Future<?> queueTask;

    /**
     * Heartbeat Thread
     */
    protected final Future<?> heartbeatTask;

    /**
     * is Closed?
     */
    protected volatile boolean isClosed = false;

    /**
     * Command Queue
     */
    protected final BlockingQueue<String> commandQueue = new ArrayBlockingQueue<>(200);

    /**
     * Holds the subscribed topics in case we need to reconnect
     */
    protected final Set<PubSubRequest> subscribedTopics = ConcurrentHashMap.newKeySet();

    /**
     * Last Ping send (1 minute delay before sending the first ping)
     */
    protected volatile long lastPing = TimeUtils.getCurrentTimeInMillis() - 4 * 60 * 1000;

    /**
     * Last Pong received
     */
    protected volatile long lastPong = TimeUtils.getCurrentTimeInMillis();

    /**
     * Thread Pool Executor
     */
    protected final ScheduledExecutorService taskExecutor;

    /**
     * WebSocket Factory
     */
    protected final WebSocketFactory webSocketFactory;

    /**
     * Helper class to compute delays between connection retries.
     * <p>
     * Configured to (approximately) emulate first-party clients:
     * <ul>
     *     <li>initially waits one second</li>
     *     <li>plus a small random jitter</li>
     *     <li>doubles the backoff period on subsequent failures</li>
     *     <li>up to a maximum backoff threshold of 2 minutes</li>
     * </ul>
     *
     * @see <a href="https://dev.twitch.tv/docs/pubsub#connection-management">Official documentation - Handling Connection Failures</a>
     */
    protected final ExponentialBackoffStrategy backoff = ExponentialBackoffStrategy.builder()
        .immediateFirst(false)
        .baseMillis(Duration.ofSeconds(1).toMillis())
        .jitter(true)
        .multiplier(2.0)
        .maximumBackoff(Duration.ofMinutes(2).toMillis())
        .build();

    /**
     * Constructor
     *
     * @param eventManager EventManager
     * @param taskExecutor ScheduledThreadPoolExecutor
     * @param proxyConfig  ProxyConfig
     */
    public TwitchPubSub(EventManager eventManager, ScheduledThreadPoolExecutor taskExecutor, ProxyConfig proxyConfig) {
        this.taskExecutor = taskExecutor;
        this.eventManager = eventManager;
        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j-pubsub", this);

        // WebSocket Factory and proxy settings
        this.webSocketFactory = new WebSocketFactory();
        if (proxyConfig != null)
            proxyConfig.applyWs(webSocketFactory.getProxySettings());

        // connect
        this.connect();

        // Run heartbeat every 4 minutes
        heartbeatTask = taskExecutor.scheduleAtFixedRate(() -> {
            if (isClosed)
                return;

            PubSubRequest request = new PubSubRequest();
            request.setType(PubSubType.PING);
            sendCommand(TypeConvert.objectToJson(request));

            log.debug("PubSub: Sending PING!");
            lastPing = TimeUtils.getCurrentTimeInMillis();
        }, 0, 4L, TimeUnit.MINUTES);

        // Runnable for flushing the command queue
        this.flushCommand = () -> {
            // Only allow a single thread to flush at a time
            if (flushing.getAndSet(true))
                return;

            // Attempt to flush the queue
            while (!isClosed) {
                try {
                    // check for missing pong response
                    if (lastPong < lastPing && TimeUtils.getCurrentTimeInMillis() >= lastPing + 10000) {
                        log.warn("PubSub: Didn't receive a PONG response in time, reconnecting to obtain a connection to a different server.");
                        reconnect();
                    }

                    // If connected, send one message from the queue
                    if (connectionState.equals(TMIConnectionState.CONNECTED)) {
                        String command = commandQueue.poll();
                        if (command != null) {
                            sendCommand(command);
                            // Logging
                            log.debug("Processed command from queue: [{}].", command);
                        } else {
                            break; // try again later
                        }
                    } else {
                        break; // try again later
                    }
                } catch (Exception ex) {
                    log.error("PubSub: Unexpected error in worker thread", ex);
                }
            }

            // Indicate that flushing has completed
            flushRequested.set(false);
            flushing.set(false);
        };

        // queue command worker
        this.queueTask = taskExecutor.scheduleWithFixedDelay(flushCommand, 0, 2500L, TimeUnit.MILLISECONDS);

        log.debug("PubSub: Started Queue Worker Thread");
    }

    /**
     * Connecting to IRC-WS
     */
    @Synchronized
    public void connect() {
        if (connectionState.equals(TMIConnectionState.DISCONNECTED) || connectionState.equals(TMIConnectionState.RECONNECTING)) {
            try {
                // Change Connection State
                connectionState = TMIConnectionState.CONNECTING;

                // Recreate Socket if state does not equal CREATED
                createWebSocket();

                // Connect to IRC WebSocket
                this.webSocket.connect();
            } catch (Exception ex) {
                log.error("PubSub: Connection to Twitch PubSub failed: {} - Retrying ...", ex.getMessage());

                // Sleep 1 second before trying to reconnect
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (Exception ignored) {

                } finally {
                    // reconnect
                    reconnect();
                }
            }
        }
    }

    /**
     * Disconnecting from WebSocket
     */
    @Synchronized
    public void disconnect() {
        if (connectionState.equals(TMIConnectionState.CONNECTED)) {
            connectionState = TMIConnectionState.DISCONNECTING;
        }

        connectionState = TMIConnectionState.DISCONNECTED;

        // CleanUp
        this.webSocket.clearListeners();
        this.webSocket.disconnect();
        this.webSocket = null;
    }

    /**
     * Reconnecting to WebSocket
     */
    @Synchronized
    public void reconnect() {
        connectionState = TMIConnectionState.RECONNECTING;
        disconnect();
        connect();
    }

    /**
     * Recreate the WebSocket and the listeners
     */
    @Synchronized
    private void createWebSocket() {
        try {
            // WebSocket
            this.webSocket = webSocketFactory.createSocket(WEB_SOCKET_SERVER);

            // WebSocket Listeners
            this.webSocket.clearListeners();
            this.webSocket.addListener(new WebSocketAdapter() {

                @Override
                public void onConnected(WebSocket ws, Map<String, List<String>> headers) {
                    log.info("Connecting to Twitch PubSub {}", WEB_SOCKET_SERVER);

                    // Connection Success
                    connectionState = TMIConnectionState.CONNECTED;
                    backoff.reset();

                    log.info("Connected to Twitch PubSub {}", WEB_SOCKET_SERVER);

                    // resubscribe to all topics after disconnect
                    subscribedTopics.forEach(topic -> listenOnTopic(topic));
                }

                @Override
                public void onTextMessage(WebSocket ws, String text) {
                    try {
                        log.trace("Received WebSocketMessage: " + text);

                        // parse message
                        PubSubResponse message = TypeConvert.jsonToObject(text, PubSubResponse.class);
                        if (message.getType().equals(PubSubType.MESSAGE)) {
                            String topic = message.getData().getTopic();
                            String type = message.getData().getMessage().getType();
                            JsonNode msgData = message.getData().getMessage().getMessageData();
                            String rawMessage = message.getData().getMessage().getRawMessage();

                            // Handle Messages
                            if (topic.startsWith("channel-bits-events-v2")) {
                                eventManager.publish(new ChannelBitsEvent(TypeConvert.convertValue(msgData, ChannelBitsData.class)));
                            } else if (topic.startsWith("channel-bits-badge-unlocks")) {
                                eventManager.publish(new ChannelBitsBadgeUnlockEvent(TypeConvert.jsonToObject(rawMessage, BitsBadgeData.class)));
                            } else if (topic.startsWith("channel-subscribe-events-v1")) {
                                eventManager.publish(new ChannelSubscribeEvent(TypeConvert.jsonToObject(rawMessage, SubscriptionData.class)));
                            } else if (topic.startsWith("channel-commerce-events-v1")) {
                                eventManager.publish(new ChannelCommerceEvent(TypeConvert.jsonToObject(rawMessage, CommerceData.class)));
                            } else if (topic.startsWith("whispers") && (type.equals("whisper_sent") || type.equals("whisper_received"))) {
                                // Whisper data is escaped Json cast into a String
                                JsonNode msgDataParsed = TypeConvert.jsonToObject(msgData.asText(), JsonNode.class);

                                //TypeReference<T> allows type parameters (unlike Class<T>) and avoids needing @SuppressWarnings("unchecked")
                                Map<String, Object> tags = TypeConvert.convertValue(msgDataParsed.path("tags"), new TypeReference<Map<String, Object>>() {});

                                String fromId = msgDataParsed.get("from_id").asText();
                                String displayName = (String) tags.get("display_name");
                                EventUser eventUser = new EventUser(fromId, displayName);

                                String body = msgDataParsed.get("body").asText();

                                Set<CommandPermission> permissions = TwitchUtils.getPermissionsFromTags(tags);

                                PrivateMessageEvent privateMessageEvent = new PrivateMessageEvent(eventUser, body, permissions);
                                eventManager.publish(privateMessageEvent);

                            } else if (topic.startsWith("community-points-channel-v1")) {
                                String timestampText = msgData.path("timestamp").asText();
                                Instant instant = Instant.parse(timestampText);
                                Calendar timestamp = GregorianCalendar.from(
                                    ZonedDateTime.ofInstant(
                                        instant,
                                        ZoneId.systemDefault()
                                    )
                                );

                                switch (type) {
                                    case "reward-redeemed":
                                        ChannelPointsRedemption redemption = TypeConvert.convertValue(msgData.path("redemption"), ChannelPointsRedemption.class);
                                        eventManager.publish(new RewardRedeemedEvent(timestamp, redemption));
                                        break;
                                    case "redemption-status-update":
                                        ChannelPointsRedemption updatedRedemption = TypeConvert.convertValue(msgData.path("redemption"), ChannelPointsRedemption.class);
                                        eventManager.publish(new RedemptionStatusUpdateEvent(timestamp, updatedRedemption));
                                        break;
                                    case "custom-reward-created":
                                        ChannelPointsReward newReward = TypeConvert.convertValue(msgData.path("new_reward"), ChannelPointsReward.class);
                                        eventManager.publish(new CustomRewardCreatedEvent(instant, newReward));
                                        break;
                                    case "custom-reward-updated":
                                        ChannelPointsReward updatedReward = TypeConvert.convertValue(msgData.path("updated_reward"), ChannelPointsReward.class);
                                        eventManager.publish(new CustomRewardUpdatedEvent(instant, updatedReward));
                                        break;
                                    case "custom-reward-deleted":
                                        ChannelPointsReward deletedReward = TypeConvert.convertValue(msgData.path("deleted_reward"), ChannelPointsReward.class);
                                        eventManager.publish(new CustomRewardDeletedEvent(instant, deletedReward));
                                        break;
                                    case "update-redemption-statuses-progress":
                                        RedemptionProgress redemptionProgress = TypeConvert.convertValue(msgData.path("progress"), RedemptionProgress.class);
                                        eventManager.publish(new UpdateRedemptionProgressEvent(instant, redemptionProgress));
                                        break;
                                    case "update-redemption-statuses-finished":
                                        RedemptionProgress redemptionFinished = TypeConvert.convertValue(msgData.path("progress"), RedemptionProgress.class);
                                        eventManager.publish(new UpdateRedemptionFinishedEvent(instant, redemptionFinished));
                                        break;
                                    default:
                                        log.warn("Unparseable Message: " + message.getType() + "|" + message.getData());
                                        break;
                                }

                            } else if (topic.startsWith("raid")) {
                                switch (type) {
                                    case "raid_go_v2":
                                        eventManager.publish(TypeConvert.jsonToObject(rawMessage, RaidGoEvent.class));
                                        break;
                                    case "raid_update_v2":
                                        eventManager.publish(TypeConvert.jsonToObject(rawMessage, RaidUpdateEvent.class));
                                        break;
                                    case "raid_cancel_v2":
                                        eventManager.publish(TypeConvert.jsonToObject(rawMessage, RaidCancelEvent.class));
                                        break;
                                    default:
                                        log.warn("Unparseable Message: " + message.getType() + "|" + message.getData());
                                        break;
                                }

                            } else if (topic.startsWith("chat_moderator_actions")) {
                                String channelId = topic.substring(topic.lastIndexOf('.') + 1);
                                ChatModerationAction data = TypeConvert.convertValue(msgData, ChatModerationAction.class);
                                eventManager.publish(new ChatModerationEvent(channelId, data));
                            } else if (topic.startsWith("following")) {
                                final String channelId = topic.substring(topic.lastIndexOf('.') + 1);
                                final FollowingData data = TypeConvert.jsonToObject(rawMessage, FollowingData.class);
                                eventManager.publish(new FollowingEvent(channelId, data));
                            } else if (topic.startsWith("hype-train-events-v1.rewards")) {
                                eventManager.publish(new HypeTrainRewardsEvent(TypeConvert.convertValue(msgData, HypeTrainRewardsData.class)));
                            } else if (topic.startsWith("hype-train-events-v1")) {
                                final String channelId = topic.substring(topic.lastIndexOf('.') + 1);
                                switch (type) {
                                    case "hype-train-start":
                                        final HypeTrainStart startData = TypeConvert.convertValue(msgData, HypeTrainStart.class);
                                        eventManager.publish(new HypeTrainStartEvent(startData));
                                        break;
                                    case "hype-train-progression":
                                        final HypeProgression progressionData = TypeConvert.convertValue(msgData, HypeProgression.class);
                                        eventManager.publish(new HypeTrainProgressionEvent(channelId, progressionData));
                                        break;
                                    case "hype-train-level-up":
                                        final HypeLevelUp levelUpData = TypeConvert.convertValue(msgData, HypeLevelUp.class);
                                        eventManager.publish(new HypeTrainLevelUpEvent(channelId, levelUpData));
                                        break;
                                    case "hype-train-end":
                                        final HypeTrainEnd endData = TypeConvert.convertValue(msgData, HypeTrainEnd.class);
                                        eventManager.publish(new HypeTrainEndEvent(channelId, endData));
                                        break;
                                    case "hype-train-conductor-update":
                                        final HypeTrainConductor conductorData = TypeConvert.convertValue(msgData, HypeTrainConductor.class);
                                        eventManager.publish(new HypeTrainConductorUpdateEvent(channelId, conductorData));
                                        break;
                                    case "hype-train-cooldown-expiration":
                                        eventManager.publish(new HypeTrainCooldownExpirationEvent(channelId));
                                        break;
                                    default:
                                        log.warn("Unparseable Message: " + message.getType() + "|" + message.getData());
                                        break;
                                }
                            } else if (topic.startsWith("community-points-user-v1")) {
                                switch (type) {
                                    case "points-earned":
                                        final ChannelPointsEarned pointsEarned = TypeConvert.convertValue(msgData, ChannelPointsEarned.class);
                                        eventManager.publish(new PointsEarnedEvent(pointsEarned));
                                        break;
                                    case "claim-available":
                                        final ClaimData claimAvailable = TypeConvert.convertValue(msgData, ClaimData.class);
                                        eventManager.publish(new ClaimAvailableEvent(claimAvailable));
                                        break;
                                    case "claim-claimed":
                                        final ClaimData claimClaimed = TypeConvert.convertValue(msgData, ClaimData.class);
                                        eventManager.publish(new ClaimClaimedEvent(claimClaimed));
                                        break;
                                    case "points-spent":
                                        final PointsSpent pointsSpent = TypeConvert.convertValue(msgData, PointsSpent.class);
                                        eventManager.publish(new PointsSpentEvent(pointsSpent));
                                        break;
                                    case "reward-redeemed":
                                        final Calendar timestamp = GregorianCalendar.from(ZonedDateTime.ofInstant(Instant.parse(msgData.path("timestamp").asText()), ZoneId.systemDefault()));
                                        final ChannelPointsRedemption redemption = TypeConvert.convertValue(msgData.path("redemption"), ChannelPointsRedemption.class);
                                        eventManager.publish(new RewardRedeemedEvent(timestamp, redemption));
                                        break;
                                    case "global-last-viewed-content-updated":
                                    case "channel-last-viewed-content-updated":
                                        // unimportant
                                        break;
                                    default:
                                        log.warn("Unparseable Message: " + message.getType() + "|" + message.getData());
                                        break;
                                }
                            } else if (topic.startsWith("leaderboard-events-v1")) {
                                final Leaderboard leaderboard = TypeConvert.jsonToObject(rawMessage, Leaderboard.class);
                                switch (leaderboard.getIdentifier().getDomain()) {
                                    case "bits-usage-by-channel-v1":
                                        eventManager.publish(new BitsLeaderboardEvent(leaderboard));
                                        break;
                                    case "sub-gifts-sent":
                                        eventManager.publish(new SubLeaderboardEvent(leaderboard));
                                        break;
                                    default:
                                        log.warn("Unparseable Message: " + message.getType() + "|" + message.getData());
                                        break;
                                }
                            } else if (topic.startsWith("polls")) {
                                PollData pollData = TypeConvert.convertValue(msgData.path("poll"), PollData.class);
                                eventManager.publish(new PollsEvent(type, pollData));
                            } else if (topic.startsWith("friendship")) {
                                eventManager.publish(new FriendshipEvent(TypeConvert.jsonToObject(rawMessage, FriendshipData.class)));
                            } else if (topic.startsWith("presence")) {
                                if ("presence".equalsIgnoreCase(type)) {
                                    eventManager.publish(new UserPresenceEvent(TypeConvert.convertValue(msgData, PresenceData.class)));
                                } else if ("settings".equalsIgnoreCase(type)) {
                                    String userId = topic.substring(topic.indexOf('.') + 1);
                                    PresenceSettings presenceSettings = TypeConvert.convertValue(msgData, PresenceSettings.class);
                                    eventManager.publish(new PresenceSettingsEvent(userId, presenceSettings));
                                } else {
                                    log.warn("Unparseable Message: " + message.getType() + "|" + message.getData());
                                }
                            } else if (topic.startsWith("channel-sub-gifts-v1")) {
                                eventManager.publish(new ChannelSubGiftEvent(TypeConvert.jsonToObject(rawMessage, SubGiftData.class)));
                            } else if (topic.startsWith("channel-cheer-events-public-v1")) {
                                String channelId = topic.substring(topic.indexOf('.') + 1);
                                if ("cheerbomb".equalsIgnoreCase(type)) {
                                    CheerbombData cheerbomb = TypeConvert.convertValue(msgData, CheerbombData.class);
                                    eventManager.publish(new CheerbombEvent(channelId, cheerbomb));
                                } else {
                                    log.warn("Unparseable Message: " + message.getType() + "|" + message.getData());
                                }
                            } else if (topic.startsWith("onsite-notifications")) {
                                if ("create-notification".equalsIgnoreCase(type)) {
                                    eventManager.publish(new OnsiteNotificationCreationEvent(TypeConvert.convertValue(msgData, CreateNotificationData.class)));
                                } else if ("update-summary".equalsIgnoreCase(type)) {
                                    String id = topic.substring(topic.indexOf('.') + 1);
                                    UpdateSummaryData data = TypeConvert.convertValue(msgData, UpdateSummaryData.class);
                                    eventManager.publish(new UpdateOnsiteNotificationSummaryEvent(id, data));
                                } else {
                                    log.warn("Unparseable Message: " + message.getType() + "|" + message.getData());
                                }
                            } else if (topic.startsWith("video-playback")) {
                                int dot = topic.indexOf('.');
                                String channel = topic.substring(dot + 1);
                                boolean hasId = topic.charAt(dot - 1) == 'd';
                                VideoPlaybackData data = TypeConvert.jsonToObject(rawMessage, VideoPlaybackData.class);
                                eventManager.publish(new VideoPlaybackEvent(hasId ? channel : null, hasId ? null : channel, data));
                            } else {
                                log.warn("Unparseable Message: " + message.getType() + "|" + message.getData());
                            }

                        } else if (message.getType().equals(PubSubType.RESPONSE)) {
                            // topic subscription success or failed, response to listen command
                            // System.out.println(message.toString());
                            if (message.getError().length() > 0) {
                                if (message.getError().equalsIgnoreCase("ERR_BADAUTH")) {
                                    log.error("PubSub: You used a invalid oauth token to subscribe to the topic. Please use a token that is authorized for the specified channel.");
                                } else {
                                    log.error("PubSub: Failed to subscribe to topic - [" + message.getError() + "]");
                                }
                            }

                        } else if (message.getType().equals(PubSubType.PONG)) {
                            log.debug("PubSub: Received PONG response!");
                            lastPong = TimeUtils.getCurrentTimeInMillis();
                        } else if (message.getType().equals(PubSubType.RECONNECT)) {
                            log.warn("PubSub: Server instance we're connected to will go down for maintenance soon, reconnecting to obtain a new connection!");
                            reconnect();
                        } else {
                            // unknown message
                            log.debug("PubSub: Unknown Message Type: " + message.toString());
                        }
                    } catch (Exception ex) {
                        log.warn("PubSub: Unparsable Message: " + text + " - [" + ex.getMessage() + "]");
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onDisconnected(WebSocket websocket,
                                           WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                           boolean closedByServer) {
                    if (!connectionState.equals(TMIConnectionState.DISCONNECTING)) {
                        log.info("Connection to Twitch PubSub lost (WebSocket)! Retrying soon ...");

                        // connection lost - reconnecting
                        taskExecutor.schedule(() -> reconnect(), backoff.get(), TimeUnit.MILLISECONDS);
                    } else {
                        connectionState = TMIConnectionState.DISCONNECTED;
                        log.info("Disconnected from Twitch PubSub (WebSocket)!");
                    }
                }

            });


        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * Send WS Message
     *
     * @param command IRC Command
     */
    private void sendCommand(String command) {
        // will send command if connection has been established
        if (connectionState.equals(TMIConnectionState.CONNECTED) || connectionState.equals(TMIConnectionState.CONNECTING)) {
            // command will be uppercase.
            this.webSocket.sendText(command);
        } else {
            log.warn("Can't send IRC-WS Command [{}]", command);
        }
    }

    /**
     * Queue PubSub request
     *
     * @param request PubSub request (or Topic)
     */
    private void queueRequest(PubSubRequest request) {
        commandQueue.add(TypeConvert.objectToJson(request));

        // Expedite command execution if we aren't already flushing the queue and another expedition hasn't already been requested
        if (!flushing.get() && !flushRequested.getAndSet(true))
            taskExecutor.schedule(this.flushCommand, 50L, TimeUnit.MILLISECONDS); // allow for some accumulation of requests before flushing
    }

    /**
     * Send WS Message to subscribe to a topic
     *
     * @param request Topic
     * @return PubSubSubscription
     */
    public PubSubSubscription listenOnTopic(PubSubRequest request) {
        if (subscribedTopics.add(request))
            queueRequest(request);
        return new PubSubSubscription(request);
    }

    public PubSubSubscription listenOnTopic(PubSubType type, OAuth2Credential credential, List<String> topics) {
        PubSubRequest request = new PubSubRequest();
        request.setType(type);
        request.setNonce(CryptoUtils.generateNonce(32));
        request.getData().put("auth_token", credential != null ? credential.getAccessToken() : "");
        request.getData().put("topics", topics);

        return listenOnTopic(request);
    }

    public PubSubSubscription listenOnTopic(PubSubType type, OAuth2Credential credential, String topic) {
        return listenOnTopic(type, credential, Collections.singletonList(topic));
    }

    public PubSubSubscription listenOnTopic(PubSubType type, OAuth2Credential credential, String... topics) {
        return listenOnTopic(type, credential, Arrays.asList(topics));
    }

    /**
     * Unsubscribe from a topic.
     * Usage example:
     * <pre>
     *      PubSubSubscription subscription = twitchPubSub.listenForCheerEvents(...);
     *      // ...
     *      twitchPubSub.unsubscribeFromTopic(subscription);
     * </pre>
     *
     * @param subscription Subscription
     */
    public void unsubscribeFromTopic(PubSubSubscription subscription) {
        PubSubRequest request = subscription.getRequest();
        if (request.getType() != PubSubType.LISTEN) {
            log.warn("Cannot unsubscribe using request with unexpected type: {}", request.getType());
            return;
        }
        boolean removed = subscribedTopics.remove(request);
        if (!removed) {
            log.warn("Not subscribed to topic: {}", request);
            return;
        }

        // use data from original request and send UNLISTEN
        PubSubRequest unlistenRequest = new PubSubRequest();
        unlistenRequest.setType(PubSubType.UNLISTEN);
        unlistenRequest.setNonce(CryptoUtils.generateNonce(32));
        unlistenRequest.setData(request.getData());
        queueRequest(unlistenRequest);
    }

    /**
     * Event Listener: User earned a new Bits badge and shared the notification with chat
     *
     * @param credential Credential (for target user id, scope: bits:read)
     * @param userId     Target User Id
     * @return PubSubSubscription
     */
    public PubSubSubscription listenForBitsBadgeEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-bits-badge-unlocks." + userId);
    }

    /**
     * Event Listener: Anyone cheers on a specified channel.
     *
     * @param credential Credential (for target user id, scope: bits:read)
     * @param userId     Target User Id
     * @return PubSubSubscription
     */
    public PubSubSubscription listenForCheerEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-bits-events-v2." + userId);
    }

    /**
     * Event Listener: Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
     *
     * @param credential Credential (for targetUserId, scope: channel_subscriptions)
     * @param userId     Target User Id
     * @return PubSubSubscription
     */
    public PubSubSubscription listenForSubscriptionEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-subscribe-events-v1." + userId);
    }

    /**
     * Event Listener: Anyone makes a purchase on a channel.
     *
     * @param credential Credential (any)
     * @param userId     Target User Id
     * @return PubSubSubscription
     */
    @Deprecated
    public PubSubSubscription listenForCommerceEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-commerce-events-v1." + userId);
    }

    /**
     * Event Listener: Anyone whispers the specified user.
     *
     * @param credential Credential (for targetUserId, scope: whispers:read)
     * @param userId     Target User Id
     * @return PubSubSubscription
     */
    public PubSubSubscription listenForWhisperEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "whispers." + userId);
    }

    /**
     * Event Listener: A moderator performs an action in the channel
     *
     * @param credential Credential (for channelId, scope: channel:moderate)
     * @param channelId  Target Channel Id
     * @return PubSubSubscription
     */
    public PubSubSubscription listenForModerationEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "chat_moderator_actions." + channelId);
    }

    /**
     * Event Listener: A moderator performs an action in the channel
     *
     * @param credential Credential (for userId, scope: channel:moderate)
     * @param userId     The user id associated with the credential
     * @param roomId     The user id associated with the target channel
     * @return PubSubSubscription
     */
    public PubSubSubscription listenForModerationEvents(OAuth2Credential credential, String userId, String roomId) {
        return listenForModerationEvents(credential, userId + "." + roomId);
    }

    /**
     * Event Listener: Anyone makes a channel points redemption on a channel.
     *
     * @param credential Credential (any)
     * @param channelId  Target Channel Id
     * @return PubSubSubscription
     */
    public PubSubSubscription listenForChannelPointsRedemptionEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "community-points-channel-v1." + channelId);
    }

    /*
     * Undocumented topics - Use at your own risk
     */

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForAdsEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "ads." + userId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForAdPropertyRefreshEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "ad-property-refresh." + userId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForBountyBoardEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-bounty-board-events.cta." + channelId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForDashboardActivityFeedEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "dashboard-activity-feed." + userId);
    }

    @Unofficial
    public PubSubSubscription listenForUserChannelPointsEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "community-points-user-v1." + userId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForChannelDropEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-drop-events." + channelId);
    }

    @Unofficial
    public PubSubSubscription listenForChannelBitsLeaderboardEvents(OAuth2Credential credential, String channelId) {
        return listenForChannelBitsLeaderboardEvents(credential, channelId, "WEEK");
    }

    @Unofficial
    public PubSubSubscription listenForChannelBitsLeaderboardMonthlyEvents(OAuth2Credential credential, String channelId) {
        return listenForChannelBitsLeaderboardEvents(credential, channelId, "MONTH");
    }

    @Unofficial
    private PubSubSubscription listenForChannelBitsLeaderboardEvents(OAuth2Credential credential, String channelId, String timeAggregationUnit) {
        return listenOnTopic(PubSubType.LISTEN, credential, "leaderboard-events-v1.bits-usage-by-channel-v1-" + channelId + "-" + timeAggregationUnit);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForChannelPrimeGiftStatusEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-prime-gifting-status." + channelId);
    }

    @Unofficial
    public PubSubSubscription listenForChannelSubLeaderboardEvents(OAuth2Credential credential, String channelId) {
        return listenForChannelSubLeaderboardEvents(credential, channelId, "WEEK");
    }

    @Unofficial
    public PubSubSubscription listenForChannelSubLeaderboardMonthlyEvents(OAuth2Credential credential, String channelId) {
        return listenForChannelSubLeaderboardEvents(credential, channelId, "MONTH");
    }

    @Unofficial
    private PubSubSubscription listenForChannelSubLeaderboardEvents(OAuth2Credential credential, String channelId, String timeAggregationUnit) {
        return listenOnTopic(PubSubType.LISTEN, credential, "leaderboard-events-v1.sub-gift-sent-" + channelId + "-" + timeAggregationUnit);
    }

    @Unofficial
    public PubSubSubscription listenForLeaderboardEvents(OAuth2Credential credential, String channelId) {
        return listenForLeaderboardEvents(credential, channelId, "WEEK");
    }

    @Unofficial
    public PubSubSubscription listenForLeaderboardMonthlyEvents(OAuth2Credential credential, String channelId) {
        return listenForLeaderboardEvents(credential, channelId, "MONTH");
    }

    @Unofficial
    private PubSubSubscription listenForLeaderboardEvents(OAuth2Credential credential, String channelId, String timeAggregationUnit) {
        return listenOnTopic(
            PubSubType.LISTEN,
            credential,
            "leaderboard-events-v1.bits-usage-by-channel-v1-" + channelId + "-" + timeAggregationUnit,
            "leaderboard-events-v1.sub-gift-sent-" + channelId + "-" + timeAggregationUnit
        );
    }

    @Unofficial
    public PubSubSubscription listenForChannelSubGiftsEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-sub-gifts-v1." + channelId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForChannelSquadEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-squad-updates." + channelId);
    }

    @Unofficial
    public PubSubSubscription listenForRaidEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "raid." + channelId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForChannelExtensionEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-ext-v1." + channelId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForExtensionControlEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "extension-control." + channelId);
    }

    @Unofficial
    public PubSubSubscription listenForHypeTrainEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "hype-train-events-v1." + channelId);
    }

    @Unofficial
    public PubSubSubscription listenForHypeTrainRewardEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "hype-train-events-v1.rewards." + channelId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForBroadcastSettingUpdateEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "broadcast-settings-update." + channelId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForCelebrationEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "celebration-events-v1." + channelId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForPublicBitEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-bit-events-public." + channelId);
    }

    @Unofficial
    public PubSubSubscription listenForPublicCheerEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "channel-cheer-events-public-v1." + channelId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForStreamChangeEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "stream-change-by-channel." + channelId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForStreamChatRoomEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "stream-chat-room-v1." + channelId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForChannelChatroomEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "chatrooms-channel-v1." + channelId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForUserChatroomEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "chatrooms-user-v1." + userId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForUserBitsUpdateEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-bits-updates-v1." + userId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForUserCampaignEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-campaign-events." + userId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForUserPropertiesUpdateEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-properties-update." + userId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForUserSubscribeEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-subscribe-events-v1." + userId);
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForUserImageUpdateEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "user-image-update." + userId);
    }

    /**
     * Event Listener: Anyone follows the specified channel.
     *
     * @param credential {@link OAuth2Credential}
     * @param channelId  the id for the channel
     * @return PubSubSubscription
     */
    @Unofficial
    public PubSubSubscription listenForFollowingEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "following." + channelId);
    }

    @Unofficial
    public PubSubSubscription listenForFriendshipEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "friendship." + userId);
    }

    @Unofficial
    public PubSubSubscription listenForOnsiteNotificationEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "onsite-notifications." + userId);
    }

    @Unofficial
    public PubSubSubscription listenForPollEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "polls." + channelId);
    }

    @Unofficial
    public PubSubSubscription listenForPresenceEvents(OAuth2Credential credential, String userId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "presence." + userId);
    }

    @Unofficial
    public PubSubSubscription listenForVideoPlaybackEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "video-playback-by-id." + channelId);
    }

    @Unofficial
    public PubSubSubscription listenForVideoPlaybackByNameEvents(OAuth2Credential credential, String channelName) {
        return listenOnTopic(PubSubType.LISTEN, credential, "video-playback." + channelName.toLowerCase());
    }

    @Unofficial
    @Deprecated
    public PubSubSubscription listenForWatchPartyEvents(OAuth2Credential credential, String channelId) {
        return listenOnTopic(PubSubType.LISTEN, credential, "pv-watch-party-events." + channelId);
    }

    /**
     * Close
     */
    public void close() {
        if (!isClosed) {
            isClosed = true;
            heartbeatTask.cancel(false);
            queueTask.cancel(false);
            disconnect();
        }
    }

}
