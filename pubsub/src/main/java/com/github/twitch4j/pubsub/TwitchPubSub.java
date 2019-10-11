package com.github.twitch4j.pubsub;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import com.github.twitch4j.common.util.TimeUtils;
import com.github.twitch4j.common.util.TwitchUtils;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import com.github.twitch4j.pubsub.domain.PubSubResponse;
import com.github.twitch4j.pubsub.enums.PubSubType;
import com.github.twitch4j.pubsub.enums.TMIConnectionState;
import com.github.twitch4j.common.util.TypeConvert;
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

import java.util.*;

/**
 * Twitch PubSub
 */
@Slf4j
public class TwitchPubSub {

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
            while (true) {
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
                            // Handle Messages
                            if (message.getData().getTopic().startsWith("channel-bits-events-v1")) {
                                // todo
                            } else if (message.getData().getTopic().startsWith("channel-subscribe-events-v1")) {
                                // todo
                            } else if (message.getData().getTopic().startsWith("channel-commerce-events-v1")) {
                                // todo
                            } else if (message.getData().getTopic().startsWith("whispers")) {
                                // Whisper
                                EventUser eventUser = new EventUser((String) message.getData().getMessage().getMessageData().get("from_id"), (String) message.getData().getMessage().getMessageDataTags().get("display_name"));
                                PrivateMessageEvent privateMessageEvent = new PrivateMessageEvent(eventUser, (String) message.getData().getMessage().getMessageData().get("body"), TwitchUtils.getPermissionsFromTags(message.getData().getMessage().getMessageDataTags()));
                                eventManager.dispatchEvent(privateMessageEvent);
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
     * Send WS Message to subscribe to a topic
     *
     * @param request Topic
     */
    public void listenOnTopic(PubSubRequest request) {
        commandQueue.add(TypeConvert.objectToJson(request));
        subscribedTopics.add(request);
    }

    /**
     * Event Listener: Anyone cheers on a specified channel.
     *
     * @param credential Credential (any)
     * @param userId Target User Id
     */
    public void listenForCheerEvents(OAuth2Credential credential, Long userId) {
        PubSubRequest request = new PubSubRequest();
        request.setType(PubSubType.LISTEN);
        request.setNonce(UUID.randomUUID().toString());
        request.getData().put("auth_token", credential.getAccessToken());
        request.getData().put("topics", Arrays.asList("channel-bits-events-v1." + userId.toString()));

        listenOnTopic(request);
    }

    /**
     * Event Listener: Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
     *
     * @param credential Credential (for targetUserId, scope: channel_subscriptions)
     * @param userId Target User Id
     */
    public void listenForSubscriptionEvents(OAuth2Credential credential, Long userId) {
        PubSubRequest request = new PubSubRequest();
        request.setType(PubSubType.LISTEN);
        request.setNonce(UUID.randomUUID().toString());
        request.getData().put("auth_token", credential.getAccessToken());
        request.getData().put("topics", Arrays.asList("channel-subscribe-events-v1." + userId.toString()));

        listenOnTopic(request);
    }

    /**
     * Event Listener: Anyone makes a purchase on a channel.
     *
     * @param credential Credential (any)
     * @param userId Target User Id
     */
    public void listenForCommerceEvents(OAuth2Credential credential, Long userId) {
        PubSubRequest request = new PubSubRequest();
        request.setType(PubSubType.LISTEN);
        request.setNonce(UUID.randomUUID().toString());
        request.getData().put("auth_token", credential.getAccessToken());
        request.getData().put("topics", Arrays.asList("channel-commerce-events-v1." + userId.toString()));

        listenOnTopic(request);
    }

    /**
     * Event Listener: Anyone whispers the specified user.
     *
     * @param credential Credential (for targetUserId, scope: whispers:read)
     * @param userId Target User Id
     */
    public void listenForWhisperEvents(OAuth2Credential credential, Long userId) {
        PubSubRequest request = new PubSubRequest();
        request.setType(PubSubType.LISTEN);
        request.setNonce(UUID.randomUUID().toString());
        request.getData().put("auth_token", credential.getAccessToken());
        request.getData().put("topics", Arrays.asList("whispers." + userId.toString()));

        listenOnTopic(request);
    }

}
