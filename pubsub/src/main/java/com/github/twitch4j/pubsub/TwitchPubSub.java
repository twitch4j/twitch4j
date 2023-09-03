package com.github.twitch4j.pubsub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.client.websocket.WebsocketConnection;
import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.common.util.TimeUtils;
import com.github.twitch4j.common.util.TwitchUtils;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.AliasRestrictionUpdateData;
import com.github.twitch4j.pubsub.domain.AutomodCaughtMessageData;
import com.github.twitch4j.pubsub.domain.AutomodLevelsModified;
import com.github.twitch4j.pubsub.domain.BanSharingSettings;
import com.github.twitch4j.pubsub.domain.BannedTermAdded;
import com.github.twitch4j.pubsub.domain.BannedTermRemoved;
import com.github.twitch4j.pubsub.domain.BitsBadgeData;
import com.github.twitch4j.pubsub.domain.ChannelBitsData;
import com.github.twitch4j.pubsub.domain.ChannelPointsEarned;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import com.github.twitch4j.pubsub.domain.ChannelTermsAction;
import com.github.twitch4j.pubsub.domain.CharityCampaignStatus;
import com.github.twitch4j.pubsub.domain.CharityDonationData;
import com.github.twitch4j.pubsub.domain.ChatModerationAction;
import com.github.twitch4j.pubsub.domain.CheerbombData;
import com.github.twitch4j.pubsub.domain.ClaimData;
import com.github.twitch4j.pubsub.domain.CommerceData;
import com.github.twitch4j.pubsub.domain.CommunityBoostProgression;
import com.github.twitch4j.pubsub.domain.CommunityGoalContribution;
import com.github.twitch4j.pubsub.domain.CreateNotificationData;
import com.github.twitch4j.pubsub.domain.CreateShoutoutData;
import com.github.twitch4j.pubsub.domain.CreatedUnbanRequest;
import com.github.twitch4j.pubsub.domain.CreatorGoal;
import com.github.twitch4j.pubsub.domain.DeletePinnedChatData;
import com.github.twitch4j.pubsub.domain.FollowingData;
import com.github.twitch4j.pubsub.domain.FriendshipData;
import com.github.twitch4j.pubsub.domain.HypeLevelUp;
import com.github.twitch4j.pubsub.domain.HypeProgression;
import com.github.twitch4j.pubsub.domain.HypeTrainApproaching;
import com.github.twitch4j.pubsub.domain.HypeTrainConductor;
import com.github.twitch4j.pubsub.domain.HypeTrainEnd;
import com.github.twitch4j.pubsub.domain.HypeTrainRewardsData;
import com.github.twitch4j.pubsub.domain.HypeTrainStart;
import com.github.twitch4j.pubsub.domain.Leaderboard;
import com.github.twitch4j.pubsub.domain.LowTrustUserNewMessage;
import com.github.twitch4j.pubsub.domain.LowTrustUserTreatmentUpdate;
import com.github.twitch4j.pubsub.domain.ModeratorUnbanRequestAction;
import com.github.twitch4j.pubsub.domain.PinnedChatData;
import com.github.twitch4j.pubsub.domain.PointsSpent;
import com.github.twitch4j.pubsub.domain.PollData;
import com.github.twitch4j.pubsub.domain.PresenceData;
import com.github.twitch4j.pubsub.domain.PresenceSettings;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import com.github.twitch4j.pubsub.domain.PubSubResponse;
import com.github.twitch4j.pubsub.domain.RadioData;
import com.github.twitch4j.pubsub.domain.RedemptionProgress;
import com.github.twitch4j.pubsub.domain.ShieldModeSettings;
import com.github.twitch4j.pubsub.domain.ShieldModeStatus;
import com.github.twitch4j.pubsub.domain.SubGiftData;
import com.github.twitch4j.pubsub.domain.SubscriptionData;
import com.github.twitch4j.pubsub.domain.SupportActivityFeedData;
import com.github.twitch4j.pubsub.domain.UpdateSummaryData;
import com.github.twitch4j.pubsub.domain.UpdatedPinnedChatTiming;
import com.github.twitch4j.pubsub.domain.UpdatedUnbanRequest;
import com.github.twitch4j.pubsub.domain.UserAutomodCaughtMessage;
import com.github.twitch4j.pubsub.domain.UserModerationActionData;
import com.github.twitch4j.pubsub.domain.VideoPlaybackData;
import com.github.twitch4j.pubsub.domain.WhisperThread;
import com.github.twitch4j.pubsub.enums.PubSubType;
import com.github.twitch4j.pubsub.events.AliasRestrictionUpdateEvent;
import com.github.twitch4j.pubsub.events.AutomodCaughtMessageEvent;
import com.github.twitch4j.pubsub.events.AutomodLevelsModifiedEvent;
import com.github.twitch4j.pubsub.events.BanSharingSettingsUpdateEvent;
import com.github.twitch4j.pubsub.events.BitsLeaderboardEvent;
import com.github.twitch4j.pubsub.events.ChannelBitsBadgeUnlockEvent;
import com.github.twitch4j.pubsub.events.ChannelBitsEvent;
import com.github.twitch4j.pubsub.events.ChannelCommerceEvent;
import com.github.twitch4j.pubsub.events.ChannelSubGiftEvent;
import com.github.twitch4j.pubsub.events.ChannelSubscribeEvent;
import com.github.twitch4j.pubsub.events.ChannelTermsEvent;
import com.github.twitch4j.pubsub.events.ChannelUnbanRequestCreateEvent;
import com.github.twitch4j.pubsub.events.ChannelUnbanRequestUpdateEvent;
import com.github.twitch4j.pubsub.events.CharityCampaignDonationEvent;
import com.github.twitch4j.pubsub.events.CharityCampaignStatusEvent;
import com.github.twitch4j.pubsub.events.ChatModerationEvent;
import com.github.twitch4j.pubsub.events.CheerbombEvent;
import com.github.twitch4j.pubsub.events.ClaimAvailableEvent;
import com.github.twitch4j.pubsub.events.ClaimClaimedEvent;
import com.github.twitch4j.pubsub.events.CommunityBoostProgressionEvent;
import com.github.twitch4j.pubsub.events.CommunityGoalContributionEvent;
import com.github.twitch4j.pubsub.events.CreatorGoalEvent;
import com.github.twitch4j.pubsub.events.CrowdChantCreatedEvent;
import com.github.twitch4j.pubsub.events.CustomRewardCreatedEvent;
import com.github.twitch4j.pubsub.events.CustomRewardDeletedEvent;
import com.github.twitch4j.pubsub.events.CustomRewardUpdatedEvent;
import com.github.twitch4j.pubsub.events.FollowingEvent;
import com.github.twitch4j.pubsub.events.FriendshipEvent;
import com.github.twitch4j.pubsub.events.HypeTrainApproachingEvent;
import com.github.twitch4j.pubsub.events.HypeTrainConductorUpdateEvent;
import com.github.twitch4j.pubsub.events.HypeTrainCooldownExpirationEvent;
import com.github.twitch4j.pubsub.events.HypeTrainEndEvent;
import com.github.twitch4j.pubsub.events.HypeTrainLevelUpEvent;
import com.github.twitch4j.pubsub.events.HypeTrainProgressionEvent;
import com.github.twitch4j.pubsub.events.HypeTrainRewardsEvent;
import com.github.twitch4j.pubsub.events.HypeTrainStartEvent;
import com.github.twitch4j.pubsub.events.LowTrustUserNewMessageEvent;
import com.github.twitch4j.pubsub.events.LowTrustUserTreatmentUpdateEvent;
import com.github.twitch4j.pubsub.events.ModUnbanRequestActionEvent;
import com.github.twitch4j.pubsub.events.OnsiteNotificationCreationEvent;
import com.github.twitch4j.pubsub.events.PinnedChatCreatedEvent;
import com.github.twitch4j.pubsub.events.PinnedChatDeletedEvent;
import com.github.twitch4j.pubsub.events.PinnedChatTimingUpdatedEvent;
import com.github.twitch4j.pubsub.events.PointsEarnedEvent;
import com.github.twitch4j.pubsub.events.PointsSpentEvent;
import com.github.twitch4j.pubsub.events.PollsEvent;
import com.github.twitch4j.pubsub.events.PredictionCreatedEvent;
import com.github.twitch4j.pubsub.events.PredictionUpdatedEvent;
import com.github.twitch4j.pubsub.events.PresenceSettingsEvent;
import com.github.twitch4j.pubsub.events.PubSubAuthRevokeEvent;
import com.github.twitch4j.pubsub.events.PubSubConnectionStateEvent;
import com.github.twitch4j.pubsub.events.PubSubListenResponseEvent;
import com.github.twitch4j.pubsub.events.RadioEvent;
import com.github.twitch4j.pubsub.events.RaidCancelEvent;
import com.github.twitch4j.pubsub.events.RaidGoEvent;
import com.github.twitch4j.pubsub.events.RaidUpdateEvent;
import com.github.twitch4j.pubsub.events.RedemptionStatusUpdateEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.github.twitch4j.pubsub.events.ShieldModeBannedTermAddedEvent;
import com.github.twitch4j.pubsub.events.ShieldModeBannedTermRemovedEvent;
import com.github.twitch4j.pubsub.events.ShieldModeSettingsUpdatedEvent;
import com.github.twitch4j.pubsub.events.ShieldModeStatusUpdatedEvent;
import com.github.twitch4j.pubsub.events.ShoutoutCreatedEvent;
import com.github.twitch4j.pubsub.events.SubLeaderboardEvent;
import com.github.twitch4j.pubsub.events.SupportActivityFeedEvent;
import com.github.twitch4j.pubsub.events.UpdateOnsiteNotificationSummaryEvent;
import com.github.twitch4j.pubsub.events.UpdateRedemptionFinishedEvent;
import com.github.twitch4j.pubsub.events.UpdateRedemptionProgressEvent;
import com.github.twitch4j.pubsub.events.UserAutomodCaughtMessageEvent;
import com.github.twitch4j.pubsub.events.UserCommunityGoalContributionEvent;
import com.github.twitch4j.pubsub.events.UserModerationActionEvent;
import com.github.twitch4j.pubsub.events.UserPredictionMadeEvent;
import com.github.twitch4j.pubsub.events.UserPredictionResultEvent;
import com.github.twitch4j.pubsub.events.UserPresenceEvent;
import com.github.twitch4j.pubsub.events.UserUnbanRequestUpdateEvent;
import com.github.twitch4j.pubsub.events.VideoPlaybackEvent;
import com.github.twitch4j.pubsub.events.WhisperThreadUpdateEvent;
import com.github.twitch4j.util.IBackoffStrategy;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Twitch PubSub
 */
@Slf4j
public class TwitchPubSub implements ITwitchPubSub {

    public static final int REQUIRED_THREAD_COUNT = 1;

    private static final Pattern LISTEN_AUTH_TOKEN = Pattern.compile("(\\{.*\"type\"\\s*?:\\s*?\"LISTEN\".*\"data\"\\s*?:\\s*?\\{.*\"auth_token\"\\s*?:\\s*?\").+(\".*}\\s*?})");

    /**
     * EventManager
     */
    @Getter
    private final EventManager eventManager;

    /**
     * WebSocket Connection
     */
    private final WebsocketConnection connection;

    /**
     * The WebSocket Server
     */
    private static final String WEB_SOCKET_SERVER = "wss://pubsub-edge.twitch.tv:443";

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
    protected final BlockingQueue<String> commandQueue = new ArrayBlockingQueue<>(128);

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
     * Bot Owner IDs
     */
    private final Collection<String> botOwnerIds;

    /**
     * Constructor
     *
     * @param websocketConnection       WebsocketConnection
     * @param eventManager              EventManager
     * @param taskExecutor              ScheduledThreadPoolExecutor
     * @param proxyConfig               ProxyConfig
     * @param botOwnerIds               Bot Owner IDs
     * @param wsPingPeriod              WebSocket Ping Period
     * @param connectionBackoffStrategy WebSocket Connection Backoff Strategy
     * @param wsCloseDelay              Websocket Close Delay
     */
    public TwitchPubSub(WebsocketConnection websocketConnection, EventManager eventManager, ScheduledThreadPoolExecutor taskExecutor, ProxyConfig proxyConfig, Collection<String> botOwnerIds, int wsPingPeriod, IBackoffStrategy connectionBackoffStrategy, int wsCloseDelay) {
        this.eventManager = eventManager;
        this.taskExecutor = taskExecutor;
        this.botOwnerIds = botOwnerIds;

        // init connection
        if (websocketConnection == null) {
            this.connection = new WebsocketConnection(spec -> {
                spec.baseUrl(WEB_SOCKET_SERVER);
                spec.closeDelay(wsCloseDelay);
                spec.wsPingPeriod(wsPingPeriod);
                spec.onStateChanged((oldState, newState) -> eventManager.publish(new PubSubConnectionStateEvent(oldState, newState, this)));
                spec.onPreConnect(this::onPreConnect);
                spec.onConnected(this::onConnected);
                spec.onTextMessage(this::onTextMessage);
                spec.onPostDisconnect(commandQueue::clear);
                spec.taskExecutor(taskExecutor);
                spec.proxyConfig(proxyConfig);
                if (connectionBackoffStrategy != null)
                    spec.backoffStrategy(connectionBackoffStrategy);
            });
        } else {
            this.connection = websocketConnection;
        }

        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j-pubsub", this);

        // connect
        this.connect();

        // Run heartbeat every 4 minutes
        heartbeatTask = taskExecutor.scheduleAtFixedRate(() -> {
            if (isClosed || connection.getConnectionState() != WebsocketConnectionState.CONNECTED)
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
                        break;
                    }

                    // If connected, send one message from the queue
                    if (WebsocketConnectionState.CONNECTED.equals(connection.getConnectionState())) {
                        String command = commandQueue.poll();
                        if (command != null) {
                            sendCommand(command);
                            // Logging
                            if (log.isDebugEnabled()) {
                                Matcher matcher = LISTEN_AUTH_TOKEN.matcher(command);
                                String cmd = matcher.find() ? matcher.group(1) + "\u2022\u2022\u2022" + matcher.group(2) : command;
                                log.debug("Processed command from queue: [{}].", cmd);
                            }
                        } else {
                            break; // try again later
                        }
                    } else {
                        break; // try again later
                    }
                } catch (Exception ex) {
                    log.error("PubSub: Unexpected error in worker thread", ex);
                    break;
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
    public void connect() {
        connection.connect();
    }

    /**
     * Disconnecting from WebSocket
     */
    public void disconnect() {
        connection.disconnect();
    }

    /**
     * Reconnecting to WebSocket
     */
    @Synchronized
    public void reconnect() {
        connection.reconnect();
    }

    protected void onPreConnect() {
        // Reset last ping to avoid edge case loop where reconnect occurred after sending PING but before receiving PONG
        lastPong = TimeUtils.getCurrentTimeInMillis();
        lastPing = lastPong - 4 * 60 * 1000;
    }

    protected void onConnected() {
        log.info("Connected to Twitch PubSub {}", WEB_SOCKET_SERVER);

        // resubscribe to all topics after disconnect
        // This involves nonce reuse, which is bad cryptography, but not a serious problem for this context
        // To avoid reuse, we can:
        // 0) stop other threads from updating subscribedTopics
        // 1) create a new PubSubRequest for each element of subscribedTopics (with a new nonce)
        // 2) clear subscribedTopics
        // 3) allow other threads to update subscribedTopics again
        // 4) send unlisten requests for the old elements of subscribedTopics (optional?)
        // 5) call listenOnTopic for each new PubSubRequest
        subscribedTopics.forEach(this::queueRequest);
    }

    protected void onTextMessage(String text) {
        try {
            log.trace("Received WebSocketMessage: " + text);

            // parse message
            PubSubResponse message = TypeConvert.jsonToObject(text, PubSubResponse.class);
            if (message.getType().equals(PubSubType.MESSAGE)) {
                String topic = message.getData().getTopic();
                String[] topicParts = StringUtils.split(topic, '.');
                String topicName = topicParts[0];
                String lastTopicIdentifier = topicParts[topicParts.length - 1];
                String type = message.getData().getMessage().getType();
                JsonNode msgData = message.getData().getMessage().getMessageData();
                String rawMessage = message.getData().getMessage().getRawMessage();

                // Handle Messages
                if ("channel-bits-events-v2".equals(topicName)) {
                    eventManager.publish(new ChannelBitsEvent(TypeConvert.convertValue(msgData, ChannelBitsData.class)));
                } else if ("channel-bits-badge-unlocks".equals(topicName)) {
                    eventManager.publish(new ChannelBitsBadgeUnlockEvent(TypeConvert.jsonToObject(rawMessage, BitsBadgeData.class)));
                } else if ("channel-subscribe-events-v1".equals(topicName)) {
                    eventManager.publish(new ChannelSubscribeEvent(TypeConvert.jsonToObject(rawMessage, SubscriptionData.class)));
                } else if ("channel-commerce-events-v1".equals(topicName)) {
                    eventManager.publish(new ChannelCommerceEvent(TypeConvert.jsonToObject(rawMessage, CommerceData.class)));
                } else if ("whispers".equals(topicName)) {
                    // Whisper data is escaped Json cast into a String
                    JsonNode msgDataParsed = TypeConvert.jsonToObject(msgData.asText(), JsonNode.class);

                    if (type.equals("whisper_sent") || type.equals("whisper_received")) {
                        //TypeReference<T> allows type parameters (unlike Class<T>) and avoids needing @SuppressWarnings("unchecked")
                        Map<String, Object> tags = TypeConvert.convertValue(msgDataParsed.path("tags"), new TypeReference<Map<String, Object>>() {});

                        String fromId = msgDataParsed.get("from_id").asText();
                        String displayName = (String) tags.get("display_name");
                        EventUser eventUser = new EventUser(fromId, displayName);

                        String body = msgDataParsed.get("body").asText();

                        Set<CommandPermission> permissions = TwitchUtils.getPermissionsFromTags(tags, new HashMap<>(), fromId, botOwnerIds);

                        PrivateMessageEvent privateMessageEvent = new PrivateMessageEvent(eventUser, body, permissions);
                        eventManager.publish(privateMessageEvent);
                    } else if ("thread".equals(type)) {
                        WhisperThread thread = TypeConvert.convertValue(msgDataParsed, WhisperThread.class);
                        eventManager.publish(new WhisperThreadUpdateEvent(lastTopicIdentifier, thread));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("automod-levels-modification".equals(topicName) && topicParts.length > 1) {
                    if ("automod_levels_modified".equals(type)) {
                        AutomodLevelsModified data = TypeConvert.convertValue(msgData, AutomodLevelsModified.class);
                        eventManager.publish(new AutomodLevelsModifiedEvent(lastTopicIdentifier, data));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("automod-queue".equals(topicName)) {
                    if (topicParts.length == 3 && "automod_caught_message".equalsIgnoreCase(type)) {
                        AutomodCaughtMessageData data = TypeConvert.convertValue(msgData, AutomodCaughtMessageData.class);
                        eventManager.publish(new AutomodCaughtMessageEvent(topicParts[2], data));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("community-boost-events-v1".equals(topicName)) {
                    if ("community-boost-progression".equals(type)) {
                        CommunityBoostProgression progression = TypeConvert.convertValue(msgData, CommunityBoostProgression.class);
                        eventManager.publish(new CommunityBoostProgressionEvent(progression));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("community-points-channel-v1".equals(topicName) || "channel-points-channel-v1".equals(topicName)) {
                    String timestampText = msgData.path("timestamp").asText();
                    Instant instant = Instant.parse(timestampText);

                    switch (type) {
                        case "reward-redeemed":
                            ChannelPointsRedemption redemption = TypeConvert.convertValue(msgData.path("redemption"), ChannelPointsRedemption.class);
                            eventManager.publish(new RewardRedeemedEvent(instant, redemption));
                            break;
                        case "redemption-status-update":
                            ChannelPointsRedemption updatedRedemption = TypeConvert.convertValue(msgData.path("redemption"), ChannelPointsRedemption.class);
                            eventManager.publish(new RedemptionStatusUpdateEvent(instant, updatedRedemption));
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
                        case "community-goal-contribution":
                            CommunityGoalContribution contribution = TypeConvert.convertValue(msgData.path("contribution"), CommunityGoalContribution.class);
                            eventManager.publish(new CommunityGoalContributionEvent(instant, contribution));
                            break;
                        default:
                            log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                            break;
                    }

                } else if ("creator-goals-events-v1".equals(topicName)) {
                    CreatorGoal creatorGoal = TypeConvert.convertValue(msgData.path("goal"), CreatorGoal.class);
                    eventManager.publish(new CreatorGoalEvent(lastTopicIdentifier, type, creatorGoal));
                } else if ("crowd-chant-channel-v1".equals(topicName)) {
                    if ("crowd-chant-created".equals(type)) {
                        CrowdChantCreatedEvent event = TypeConvert.convertValue(msgData, CrowdChantCreatedEvent.class);
                        eventManager.publish(event);
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("raid".equals(topicName)) {
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
                            log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                            break;
                    }
                } else if ("charity-campaign-donation-events-v1".equals(topicName) && topicParts.length > 1) {
                    switch (type) {
                        case "charity_campaign_donation":
                            CharityDonationData donation = TypeConvert.jsonToObject(rawMessage, CharityDonationData.class);
                            eventManager.publish(new CharityCampaignDonationEvent(lastTopicIdentifier, donation));
                            break;
                        case "charity_campaign_status":
                            CharityCampaignStatus status = TypeConvert.jsonToObject(rawMessage, CharityCampaignStatus.class);
                            eventManager.publish(new CharityCampaignStatusEvent(lastTopicIdentifier, status));
                            break;
                        default:
                            log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                            break;
                    }
                } else if ("chat_moderator_actions".equals(topicName) && topicParts.length > 1) {
                    switch (type) {
                        case "moderation_action":
                            ChatModerationAction modAction = TypeConvert.convertValue(msgData, ChatModerationAction.class);
                            eventManager.publish(new ChatModerationEvent(lastTopicIdentifier, modAction));
                            break;

                        case "channel_terms_action":
                            ChannelTermsAction termsAction = TypeConvert.convertValue(msgData, ChannelTermsAction.class);
                            eventManager.publish(new ChannelTermsEvent(lastTopicIdentifier, termsAction));
                            break;

                        case "approve_unban_request":
                        case "deny_unban_request":
                            ModeratorUnbanRequestAction unbanRequestAction = TypeConvert.convertValue(msgData, ModeratorUnbanRequestAction.class);
                            eventManager.publish(new ModUnbanRequestActionEvent(lastTopicIdentifier, unbanRequestAction));
                            break;

                        case "moderator_added":
                        case "moderator_removed":
                        case "vip_added":
                        case "vip_removed":
                            ChatModerationAction.ModerationAction act = "moderator_added".equals(type) ? ChatModerationAction.ModerationAction.MOD
                                : "moderator_removed".equals(type) ? ChatModerationAction.ModerationAction.UNMOD
                                : "vip_added".equals(type) ? ChatModerationAction.ModerationAction.VIP
                                : ChatModerationAction.ModerationAction.UNVIP;

                            String targetUserId = msgData.path("target_user_id").asText();
                            String targetUserName = msgData.path("target_user_login").asText();
                            String createdByUserId = msgData.path("created_by_user_id").asText();
                            String createdBy = msgData.path("created_by").asText();
                            ChatModerationAction action = new ChatModerationAction("chat_login_moderation", act, Collections.singletonList(targetUserName), createdBy, createdByUserId, "", targetUserId, targetUserName, false);
                            eventManager.publish(new ChatModerationEvent(lastTopicIdentifier, action));
                            break;

                        default:
                            log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                            break;
                    }
                } else if ("chatrooms-user-v1".equals(topicName) && topicParts.length > 1) {
                    final String userId = topicParts[1];
                    switch (type) {
                        case "channel_banned_alias_restriction_update":
                            final AliasRestrictionUpdateData aliasData = TypeConvert.convertValue(msgData, AliasRestrictionUpdateData.class);
                            eventManager.publish(new AliasRestrictionUpdateEvent(userId, aliasData));
                            break;

                        case "user_moderation_action":
                            final UserModerationActionData actionData = TypeConvert.convertValue(msgData, UserModerationActionData.class);
                            eventManager.publish(new UserModerationActionEvent(userId, actionData));
                            break;

                        default:
                            log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                            break;
                    }
                } else if ("following".equals(topicName) && topicParts.length > 1) {
                    final FollowingData data = TypeConvert.jsonToObject(rawMessage, FollowingData.class);
                    eventManager.publish(new FollowingEvent(lastTopicIdentifier, data));
                } else if ("hype-train-events-v1".equals(topicName) && topicParts.length > 2 && "rewards".equals(topicParts[1])) {
                    eventManager.publish(new HypeTrainRewardsEvent(TypeConvert.convertValue(msgData, HypeTrainRewardsData.class)));
                } else if ("hype-train-events-v1".equals(topicName) && topicParts.length > 1) {
                    switch (type) {
                        case "hype-train-approaching":
                            final HypeTrainApproaching approachData = TypeConvert.convertValue(msgData, HypeTrainApproaching.class);
                            eventManager.publish(new HypeTrainApproachingEvent(approachData));
                            break;
                        case "hype-train-start":
                            final HypeTrainStart startData = TypeConvert.convertValue(msgData, HypeTrainStart.class);
                            eventManager.publish(new HypeTrainStartEvent(startData));
                            break;
                        case "hype-train-progression":
                            final HypeProgression progressionData = TypeConvert.convertValue(msgData, HypeProgression.class);
                            eventManager.publish(new HypeTrainProgressionEvent(lastTopicIdentifier, progressionData));
                            break;
                        case "hype-train-level-up":
                            final HypeLevelUp levelUpData = TypeConvert.convertValue(msgData, HypeLevelUp.class);
                            eventManager.publish(new HypeTrainLevelUpEvent(lastTopicIdentifier, levelUpData));
                            break;
                        case "hype-train-end":
                            final HypeTrainEnd endData = TypeConvert.convertValue(msgData, HypeTrainEnd.class);
                            eventManager.publish(new HypeTrainEndEvent(lastTopicIdentifier, endData));
                            break;
                        case "hype-train-conductor-update":
                            final HypeTrainConductor conductorData = TypeConvert.convertValue(msgData, HypeTrainConductor.class);
                            eventManager.publish(new HypeTrainConductorUpdateEvent(lastTopicIdentifier, conductorData));
                            break;
                        case "hype-train-cooldown-expiration":
                            eventManager.publish(new HypeTrainCooldownExpirationEvent(lastTopicIdentifier));
                            break;
                        case "last-x-experiment-event":
                            // note: this isn't a true hype train event (it can be fired with no train active), but twitch hacked together the feature to use the hype pubsub infrastructure
                            final SupportActivityFeedData lastData = TypeConvert.convertValue(msgData, SupportActivityFeedData.class);
                            eventManager.publish(new SupportActivityFeedEvent(lastTopicIdentifier, lastData));
                            break;
                        default:
                            log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                            break;
                    }
                } else if ("community-points-user-v1".equals(topicName)) {
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
                            final ChannelPointsRedemption redemption = TypeConvert.convertValue(msgData.path("redemption"), ChannelPointsRedemption.class);
                            eventManager.publish(new RewardRedeemedEvent(Instant.parse(msgData.path("timestamp").asText()), redemption));
                            break;
                        case "community-goal-contribution":
                            CommunityGoalContribution goal = TypeConvert.convertValue(msgData.path("contribution"), CommunityGoalContribution.class);
                            Instant instant = Instant.parse(msgData.path("timestamp").textValue());
                            eventManager.publish(new UserCommunityGoalContributionEvent(lastTopicIdentifier, instant, goal));
                            break;
                        case "global-last-viewed-content-updated":
                        case "channel-last-viewed-content-updated":
                            // unimportant
                            break;
                        default:
                            log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                            break;
                    }
                } else if ("leaderboard-events-v1".equals(topicName)) {
                    final Leaderboard leaderboard = TypeConvert.jsonToObject(rawMessage, Leaderboard.class);
                    switch (leaderboard.getIdentifier().getDomain()) {
                        case "bits-usage-by-channel-v1":
                            eventManager.publish(new BitsLeaderboardEvent(leaderboard));
                            break;
                        case "sub-gifts-sent":
                            eventManager.publish(new SubLeaderboardEvent(leaderboard));
                            break;
                        default:
                            log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                            break;
                    }
                } else if ("user-moderation-notifications".equals(topicName)) {
                    if (topicParts.length == 3 && "automod_caught_message".equalsIgnoreCase(type)) {
                        UserAutomodCaughtMessage data = TypeConvert.convertValue(msgData, UserAutomodCaughtMessage.class);
                        eventManager.publish(new UserAutomodCaughtMessageEvent(topicParts[1], topicParts[2], data));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("polls".equals(topicName)) {
                    PollData pollData = TypeConvert.convertValue(msgData.path("poll"), PollData.class);
                    eventManager.publish(new PollsEvent(type, pollData));
                } else if ("predictions-channel-v1".equals(topicName)) {
                    if ("event-created".equals(type)) {
                        eventManager.publish(TypeConvert.convertValue(msgData, PredictionCreatedEvent.class));
                    } else if ("event-updated".equals(type)) {
                        eventManager.publish(TypeConvert.convertValue(msgData, PredictionUpdatedEvent.class));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("predictions-user-v1".equals(topicName)) {
                    if ("prediction-made".equals(type)) {
                        eventManager.publish(TypeConvert.convertValue(msgData, UserPredictionMadeEvent.class));
                    } else if ("prediction-result".equals(type)) {
                        eventManager.publish(TypeConvert.convertValue(msgData, UserPredictionResultEvent.class));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("friendship".equals(topicName)) {
                    eventManager.publish(new FriendshipEvent(TypeConvert.jsonToObject(rawMessage, FriendshipData.class)));
                } else if ("presence".equals(topicName) && topicParts.length > 1) {
                    if ("presence".equalsIgnoreCase(type)) {
                        eventManager.publish(new UserPresenceEvent(TypeConvert.convertValue(msgData, PresenceData.class)));
                    } else if ("settings".equalsIgnoreCase(type)) {
                        PresenceSettings presenceSettings = TypeConvert.convertValue(msgData, PresenceSettings.class);
                        eventManager.publish(new PresenceSettingsEvent(lastTopicIdentifier, presenceSettings));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("radio-events-v1".equals(topicName)) {
                    // noinspection deprecation
                    eventManager.publish(new RadioEvent(TypeConvert.jsonToObject(rawMessage, RadioData.class)));
                } else if ("shield-mode".equals(topicName) && topicParts.length == 3) {
                    String userId = topicParts[1];
                    String channelId = topicParts[2];
                    switch (type) {
                        case "ADD_AUTOBAN_TERM":
                            BannedTermAdded termAdded = TypeConvert.convertValue(msgData, BannedTermAdded.class);
                            eventManager.publish(new ShieldModeBannedTermAddedEvent(userId, channelId, termAdded));
                            break;

                        case "REMOVE_AUTOBAN_TERM":
                            BannedTermRemoved termRemoved = TypeConvert.convertValue(msgData, BannedTermRemoved.class);
                            eventManager.publish(new ShieldModeBannedTermRemovedEvent(userId, channelId, termRemoved));
                            break;

                        case "UPDATE_CHANNEL_MODERATION_MODE":
                            ShieldModeStatus shieldModeStatus = TypeConvert.convertValue(msgData, ShieldModeStatus.class);
                            eventManager.publish(new ShieldModeStatusUpdatedEvent(userId, channelId, shieldModeStatus));
                            break;

                        case "UPDATE_CHANNEL_MODERATION_SETTINGS":
                            ShieldModeSettings shieldModeSettings = TypeConvert.convertValue(msgData, ShieldModeSettings.class);
                            eventManager.publish(new ShieldModeSettingsUpdatedEvent(userId, channelId, shieldModeSettings));
                            break;

                        case "UPDATE_CHANNEL_MODERATION_MODE_SHORTCUT":
                            // do nothing; is_shortcut_enabled is unimportant
                            break;

                        default:
                            log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                            break;
                    }
                } else if ("shoutout".equals(topicName)) {
                    if ("create".equalsIgnoreCase(type)) {
                        CreateShoutoutData shoutoutInfo = TypeConvert.convertValue(msgData, CreateShoutoutData.class);
                        eventManager.publish(new ShoutoutCreatedEvent(lastTopicIdentifier, shoutoutInfo));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("channel-sub-gifts-v1".equals(topicName)) {
                    eventManager.publish(new ChannelSubGiftEvent(TypeConvert.jsonToObject(rawMessage, SubGiftData.class)));
                } else if ("channel-cheer-events-public-v1".equals(topicName) && topicParts.length > 1) {
                    if ("cheerbomb".equalsIgnoreCase(type)) {
                        CheerbombData cheerbomb = TypeConvert.convertValue(msgData, CheerbombData.class);
                        eventManager.publish(new CheerbombEvent(lastTopicIdentifier, cheerbomb));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("low-trust-users".equals(topicName) && topicParts.length == 3) {
                    String userId = topicParts[1];
                    String channelId = topicParts[2];
                    if ("low_trust_user_new_message".equals(type)) {
                        eventManager.publish(new LowTrustUserNewMessageEvent(userId, channelId, TypeConvert.convertValue(msgData, LowTrustUserNewMessage.class)));
                    } else if ("low_trust_user_treatment_update".equals(type)) {
                        eventManager.publish(new LowTrustUserTreatmentUpdateEvent(userId, channelId, TypeConvert.convertValue(msgData, LowTrustUserTreatmentUpdate.class)));
                    } else if ("bans_sharing_settings_update".equals(type)) {
                        eventManager.publish(new BanSharingSettingsUpdateEvent(userId, channelId, TypeConvert.convertValue(msgData, BanSharingSettings.class)));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("onsite-notifications".equals(topicName) && topicParts.length > 1) {
                    if ("create-notification".equalsIgnoreCase(type)) {
                        eventManager.publish(new OnsiteNotificationCreationEvent(TypeConvert.convertValue(msgData, CreateNotificationData.class)));
                    } else if ("update-summary".equalsIgnoreCase(type)) {
                        UpdateSummaryData data = TypeConvert.convertValue(msgData, UpdateSummaryData.class);
                        eventManager.publish(new UpdateOnsiteNotificationSummaryEvent(lastTopicIdentifier, data));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("pinned-chat-updates-v1".equals(topicName)) {
                    switch (type) {
                        case "pin-message":
                            PinnedChatData createdPin = TypeConvert.convertValue(msgData, PinnedChatData.class);
                            eventManager.publish(new PinnedChatCreatedEvent(lastTopicIdentifier, createdPin));
                            break;
                        case "update-message":
                            UpdatedPinnedChatTiming updatedPin = TypeConvert.convertValue(msgData, UpdatedPinnedChatTiming.class);
                            eventManager.publish(new PinnedChatTimingUpdatedEvent(lastTopicIdentifier, updatedPin));
                            break;
                        case "unpin-message":
                            DeletePinnedChatData deletePin = TypeConvert.convertValue(msgData, DeletePinnedChatData.class);
                            eventManager.publish(new PinnedChatDeletedEvent(lastTopicIdentifier, deletePin));
                            break;
                        default:
                            log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                            break;
                    }
                } else if (("video-playback-by-id".equals(topicName) || "video-playback".equals(topicName)) && topicParts.length > 1) {
                    boolean hasId = topicName.endsWith("d");
                    VideoPlaybackData data = TypeConvert.jsonToObject(rawMessage, VideoPlaybackData.class);
                    eventManager.publish(new VideoPlaybackEvent(hasId ? lastTopicIdentifier : null, hasId ? null : lastTopicIdentifier, data));
                } else if ("channel-unban-requests".equals(topicName) && topicParts.length == 3) {
                    String userId = topicParts[1];
                    String channelId = topicParts[2];
                    if ("create_unban_request".equals(type)) {
                        CreatedUnbanRequest request = TypeConvert.convertValue(msgData, CreatedUnbanRequest.class);
                        eventManager.publish(new ChannelUnbanRequestCreateEvent(userId, channelId, request));
                    } else if ("update_unban_request".equals(type)) {
                        UpdatedUnbanRequest request = TypeConvert.convertValue(msgData, UpdatedUnbanRequest.class);
                        eventManager.publish(new ChannelUnbanRequestUpdateEvent(userId, channelId, request));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else if ("user-unban-requests".equals(topicName) && topicParts.length == 3) {
                    String userId = topicParts[1];
                    String channelId = topicParts[2];
                    if ("update_unban_request".equals(type)) {
                        UpdatedUnbanRequest request = TypeConvert.convertValue(msgData, UpdatedUnbanRequest.class);
                        eventManager.publish(new UserUnbanRequestUpdateEvent(userId, channelId, request));
                    } else {
                        log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                    }
                } else {
                    log.warn("Unparsable Message: " + message.getType() + "|" + message.getData());
                }

            } else if (message.getType().equals(PubSubType.RESPONSE)) {
                Supplier<PubSubRequest> findListenRequest = () -> {
                    for (PubSubRequest topic : subscribedTopics) {
                        if (topic != null && StringUtils.equals(message.getNonce(), topic.getNonce())) {
                            return topic;
                        }
                    }
                    return null;
                };

                eventManager.publish(new PubSubListenResponseEvent(message.getNonce(), message.getError(), findListenRequest));

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
            } else if (message.getType() == PubSubType.AUTH_REVOKED) {
                PubSubRequest revocation = TypeConvert.jsonToObject(text, PubSubRequest.class);
                Object topicsObj = revocation.getData().get("topics");
                if (topicsObj instanceof Collection) {
                    Map<String, PubSubRequest> revoked = new HashMap<>(); // allows for null values

                    // Read topic names
                    for (Object topicObj : (Collection<?>) topicsObj) {
                        if (topicObj instanceof String) {
                            revoked.put((String) topicObj, null);
                        } else {
                            log.warn("Unparsable Revocation Topic: {}", topicObj);
                        }
                    }

                    if (revoked.isEmpty())
                        return; // should not occur

                    // Unsubscribe
                    subscribedTopics.removeIf(req -> {
                        Object topics = req.getData().get("topics");
                        if (topics instanceof Collection && ((Collection<?>) topics).size() == 1) {
                            Object topic = ((Collection<?>) topics).iterator().next();
                            return topic instanceof String && revoked.replace((String) topic, null, req);
                        }
                        return false;
                    });

                    // Fire event
                    eventManager.publish(new PubSubAuthRevokeEvent(this, Collections.unmodifiableMap(revoked)));
                } else {
                    log.warn("Unparsable Revocation: {}", text);
                }
            } else {
                // unknown message
                log.debug("PubSub: Unknown Message Type: " + message);
            }
        } catch (Exception ex) {
            log.warn("PubSub: Unparsable Message: " + text + " - [" + ex.getMessage() + "]", ex);
        }
    }

    /**
     * Send WS Message
     *
     * @param command IRC Command
     */
    private void sendCommand(String command) {
        // will send command if connection has been established
        if (WebsocketConnectionState.CONNECTED.equals(connection.getConnectionState()) || WebsocketConnectionState.CONNECTING.equals(connection.getConnectionState())) {
            connection.sendText(command);
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

    @Override
    public PubSubSubscription listenOnTopic(PubSubRequest request) {
        if (subscribedTopics.add(request)) {
            checkListenCount(request);
            queueRequest(request);
        }
        return new PubSubSubscription(request);
    }

    @Override
    public boolean unsubscribeFromTopic(PubSubSubscription subscription) {
        PubSubRequest request = subscription.getRequest();
        if (request.getType() != PubSubType.LISTEN) {
            log.warn("Cannot unsubscribe using request with unexpected type: {}", request.getType());
            return false;
        }
        boolean removed = subscribedTopics.remove(request);
        if (!removed) {
            log.warn("Not subscribed to topic: {}", request);
            return false;
        }

        // use data from original request and send UNLISTEN
        PubSubRequest unlistenRequest = new PubSubRequest();
        unlistenRequest.setType(PubSubType.UNLISTEN);
        unlistenRequest.setNonce(CryptoUtils.generateNonce(30));
        unlistenRequest.setData(request.getData());
        queueRequest(unlistenRequest);
        return true;
    }

    public long getLatency() {
        return connection.getLatency();
    }

    /**
     * Close
     */
    @SneakyThrows
    @Override
    public void close() {
        if (!isClosed) {
            isClosed = true;
            heartbeatTask.cancel(false);
            queueTask.cancel(false);
            connection.close();
        }
    }

    private void checkListenCount(PubSubRequest request) {
        Object topics = request.getData().get("topics");
        if (topics instanceof Collection && ((Collection<?>) topics).size() > 1) {
            log.warn("Listening to multiple PubSub topics in a single request is not recommended; " +
                "automatic topic management can degrade upon PubSubAuthRevokeEvent");
        }
    }

}
