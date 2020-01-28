package com.github.twitch4j.pubsub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import com.github.twitch4j.common.util.TimeUtils;
import com.github.twitch4j.common.util.TwitchUtils;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import com.github.twitch4j.pubsub.domain.PubSubResponse;
import com.github.twitch4j.pubsub.enums.PubSubType;
import com.github.twitch4j.pubsub.enums.TMIConnectionState;
import com.github.twitch4j.pubsub.events.ChannelPointsRedemptionEvent;
import com.github.twitch4j.pubsub.events.RedemptionStatusUpdateEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Twitch PubSub
 */
@Slf4j
public class TwitchPubSub implements AutoCloseable {

    /**
     * EventManager
     */
    @Getter
    private final EventManager eventManager;

    /**
     * The WebSocket Server
     */
    private final String webSocketServer = "wss://pubsub-edge.twitch.tv:443";

    /**
     * WebSocket Client
     */
    @Setter(AccessLevel.NONE)
    private WebSocket webSocket;

    /**
     * The connection state
     * Default: ({@link TMIConnectionState#DISCONNECTED})
     */
    private TMIConnectionState connectionState = TMIConnectionState.DISCONNECTED;

    /**
     * Command Queue Thread
     */
    protected final Thread queueThread;

    /**
     * is Closed?
     */
    protected boolean isClosed = false;

    /**
     * Command Queue
     */
    protected final CircularFifoQueue<String> commandQueue = new CircularFifoQueue<>(200);

    /**
     * Holds the subscribed topics in case we need to reconnect
     */
    protected final List<PubSubRequest> subscribedTopics = new ArrayList<>();

    /**
     * Last Ping send (1 minute delay before sending the first ping)
     */
    protected long lastPing = TimeUtils.getCurrentTimeInMillis() - 4 * 60 * 1000;

    /**
     * Last Pong received
     */
    protected long lastPong = TimeUtils.getCurrentTimeInMillis();

    /**
     * Constructor
     *
     * @param eventManager EventManager
     */
    public TwitchPubSub(EventManager eventManager) {
        this.eventManager = eventManager;
        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j-pubsub", this);

        // connect
        this.connect();

        // queue command worker
        this.queueThread = new Thread(() -> {
            while (isClosed == false) {
                try {
                    // check if we need to send a PING (every 4 minutes, disconnect after 5 minutes without sending ping)
                    if (TimeUtils.getCurrentTimeInMillis() - lastPing > 4 * 60 * 1000) {
                        PubSubRequest request = new PubSubRequest();
                        request.setType(PubSubType.PING);
                        sendCommand(TypeConvert.objectToJson(request));

                        log.debug("PubSub: Sending PING!");
                        lastPing = TimeUtils.getCurrentTimeInMillis();
                    }

                    // check for missing pong response
                    if (TimeUtils.getCurrentTimeInMillis() >= lastPing + 10000 && lastPong < lastPing) {
                        log.warn("PubSub: Didn't receive a PONG response in time, reconnecting to obtain a connection to a different server.");
                        reconnect();
                    }

                    // If connected, send one message from the queue
                    if (commandQueue.size() > 0) {
                        if (connectionState.equals(TMIConnectionState.CONNECTED)) {
                            // pop one command from the queue and execute it
                            String command = commandQueue.remove();
                            sendCommand(command);

                            // Logging
                            log.debug("Processed command from queue: [{}].", command);
                        }
                    }

                    // sleep one second
                    Thread.sleep(100);
                } catch (Exception ex) {
                    log.error("PubSub: Unexpected error in worker thread: " + ex.getMessage());
                }
            }
        });
        queueThread.start();
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
                    Thread.sleep(1000);
                } catch (Exception et) {

                }

                // reconnect
                reconnect();
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
    private void createWebSocket() {
        try {
            // WebSocket
            this.webSocket = new WebSocketFactory().createSocket(webSocketServer);

            // WebSocket Listeners
            this.webSocket.clearListeners();
            this.webSocket.addListener(new WebSocketAdapter() {

                @Override
                public void onConnected(WebSocket ws, Map<String, List<String>> headers) {
                    log.info("Connecting to Twitch PubSub {}", webSocketServer);

                    // Connection Success
                    connectionState = TMIConnectionState.CONNECTED;

                    log.info("Connected to Twitch PubSub {}", webSocketServer);

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

                            // Handle Messages
                            if (topic.startsWith("channel-bits-events-v1")) {
                                // todo
                            } else if (topic.startsWith("channel-subscribe-events-v1")) {
                                // todo
                            } else if (topic.startsWith("channel-commerce-events-v1")) {
                                // todo
                            } else if (topic.startsWith("whispers") && (type.equals("whisper_sent") || type.equals("whisper_received"))) {
                                // Whisper data is escaped Json cast into a String
                                JsonNode msgDataParsed = TypeConvert.jsonToObject(msgData.asText(), JsonNode.class);

                                //TypeReference<T> allows type parameters (unlike Class<T>) and avoids needing @SuppressWarnings("unchecked")
                                Map<String, Object> tags = TypeConvert.convertValue(msgDataParsed.path("tags"), new TypeReference<Map<String, Object>>(){});

                                String fromId = msgDataParsed.get("from_id").asText();
                                String displayName = (String) tags.get("display_name");
                                EventUser eventUser = new EventUser(fromId, displayName);

                                String body = msgDataParsed.get("body").asText();

                                Set<CommandPermission> permissions = TwitchUtils.getPermissionsFromTags(tags);

                                PrivateMessageEvent privateMessageEvent = new PrivateMessageEvent(eventUser, body, permissions);
                                eventManager.publish(privateMessageEvent);

                            } else if (topic.startsWith("community-points-channel-v1")) {
                                String timestampText = msgData.path("timestamp").asText();
                                Calendar timestamp = GregorianCalendar.from(
                                    ZonedDateTime.ofInstant(
                                        Instant.from(DateTimeFormatter.ISO_INSTANT.parse(timestampText)),
                                        ZoneId.systemDefault()
                                    )
                                );
                                ChannelPointsRedemption redemption = TypeConvert.convertValue(msgData.path("redemption"), ChannelPointsRedemption.class);

                                switch(type) {
                                    case "reward-redeemed": eventManager.publish(new RewardRedeemedEvent(timestamp, redemption)); break;
                                    case "redemption-status-update": eventManager.publish(new RedemptionStatusUpdateEvent(timestamp, redemption)); break;
                                    default: eventManager.publish(new ChannelPointsRedemptionEvent(timestamp, redemption));
                                }

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
                        log.info("Connection to Twitch PubSub lost (WebSocket)! Retrying ...");

                        // connection lost - reconnecting
                        reconnect();
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
     * @param request PubSub request (or Topic)
     */
    private void queueRequest(PubSubRequest request) {
        commandQueue.add(TypeConvert.objectToJson(request));
    }

    /**
     * Send WS Message to subscribe to a topic
     *
     * @param request Topic
     */
    public PubSubSubscription listenOnTopic(PubSubRequest request) {
        queueRequest(request);
        subscribedTopics.add(request);
        return new PubSubSubscription(request);
    }

    /**
     * Unsubscribe from a topic.
     * Usage example:
     * <pre>
     *      PubSubSubscription subscription = twitchPubSub.listenForCheerEvents(...);
     *      // ...
     *      twitchPubSub.unsubscribeFromTopic(subscription);
     * </pre>
     * @param subscription Subscription
     */
    public void unsubscribeFromTopic(PubSubSubscription subscription) {
        PubSubRequest request = subscription.getRequest();
        if(request.getType() != PubSubType.LISTEN) {
            log.warn("Cannot unsubscribe using request with unexpected type: {}", request.getType());
            return;
        }
        int topicIndex = subscribedTopics.indexOf(request);
        if(topicIndex == -1) {
            log.warn("Not subscribed to topic: {}", request);
            return;
        }
        subscribedTopics.remove(topicIndex);

        // use data from original request and send UNLISTEN
        PubSubRequest unlistenRequest = new PubSubRequest();
        unlistenRequest.setType(PubSubType.UNLISTEN);
        unlistenRequest.setNonce(request.getNonce());
        unlistenRequest.setData(request.getData());
        queueRequest(unlistenRequest);
    }

    /**
     * Event Listener: Anyone cheers on a specified channel.
     * @param credential Credential (any)
     * @param userId Target User Id
     * @return PubSubSubscription
     */
    public PubSubSubscription listenForCheerEvents(OAuth2Credential credential, String userId) {
        PubSubRequest request = new PubSubRequest();
        request.setType(PubSubType.LISTEN);
        request.setNonce(UUID.randomUUID().toString());
        request.getData().put("auth_token", credential.getAccessToken());
        request.getData().put("topics", Collections.singletonList("channel-bits-events-v1." + userId));

        return listenOnTopic(request);
    }

    /**
     * Event Listener: Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
     * @param credential Credential (for targetUserId, scope: channel_subscriptions)
     * @param userId Target User Id
     * @return PubSubSubscription
     */
    public PubSubSubscription listenForSubscriptionEvents(OAuth2Credential credential, String userId) {
        PubSubRequest request = new PubSubRequest();
        request.setType(PubSubType.LISTEN);
        request.setNonce(UUID.randomUUID().toString());
        request.getData().put("auth_token", credential.getAccessToken());
        request.getData().put("topics", Collections.singletonList("channel-subscribe-events-v1." + userId));

        return listenOnTopic(request);
    }

    /**
     * Event Listener: Anyone makes a purchase on a channel.
     * @param credential Credential (any)
     * @param userId Target User Id
     * @return PubSubSubscription
     */
    public PubSubSubscription listenForCommerceEvents(OAuth2Credential credential, String userId) {
        PubSubRequest request = new PubSubRequest();
        request.setType(PubSubType.LISTEN);
        request.setNonce(UUID.randomUUID().toString());
        request.getData().put("auth_token", credential.getAccessToken());
        request.getData().put("topics", Collections.singletonList("channel-commerce-events-v1." + userId));

        return listenOnTopic(request);
    }

    /**
     * Event Listener: Anyone whispers the specified user.
     * @param credential Credential (for targetUserId, scope: whispers:read)
     * @param userId Target User Id
     * @return PubSubSubscription
     */
    public PubSubSubscription listenForWhisperEvents(OAuth2Credential credential, String userId) {
        PubSubRequest request = new PubSubRequest();
        request.setType(PubSubType.LISTEN);
        request.setNonce(UUID.randomUUID().toString());
        request.getData().put("auth_token", credential.getAccessToken());
        request.getData().put("topics", Collections.singletonList("whispers." + userId));

        return listenOnTopic(request);
    }

    /**
     * Event Listener: Anyone makes a channel points redemption on a channel.
     *
     * @param credential Credential (any)
     * @param channelId Target Channel Id
     */
    public PubSubSubscription listenForChannelPointsRedemptionEvents(OAuth2Credential credential, String channelId) {
        PubSubRequest request = new PubSubRequest();
        request.setType(PubSubType.LISTEN);
        request.setNonce(UUID.randomUUID().toString());
        request.getData().put("auth_token", credential.getAccessToken());
        request.getData().put("topics", Collections.singletonList("community-points-channel-v1." + channelId));

        return listenOnTopic(request);
    }

    /**
     * Close
     */
    public void close() {
        if (!isClosed) {
            disconnect();
            isClosed = true;
        }
    }

}
