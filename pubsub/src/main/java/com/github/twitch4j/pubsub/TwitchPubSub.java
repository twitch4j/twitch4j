package com.github.twitch4j.pubsub;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.client.websocket.WebsocketConnection;
import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.common.util.TimeUtils;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import com.github.twitch4j.pubsub.domain.PubSubResponse;
import com.github.twitch4j.pubsub.domain.PubSubResponsePayload;
import com.github.twitch4j.pubsub.enums.PubSubType;
import com.github.twitch4j.pubsub.events.PubSubAuthRevokeEvent;
import com.github.twitch4j.pubsub.events.PubSubConnectionStateEvent;
import com.github.twitch4j.pubsub.events.PubSubListenResponseEvent;
import com.github.twitch4j.pubsub.handlers.HandlerRegistry;
import com.github.twitch4j.pubsub.handlers.TopicHandler;
import com.github.twitch4j.util.IBackoffStrategy;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
import java.util.function.Consumer;
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
     * Fallback Topic Handler
     */
    private final Consumer<PubSubResponsePayload> fallbackTopicHandler;

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
     * @param fallbackTopicHandler      Fallback Topic Handler
     */
    public TwitchPubSub(WebsocketConnection websocketConnection, EventManager eventManager, ScheduledThreadPoolExecutor taskExecutor, ProxyConfig proxyConfig, Collection<String> botOwnerIds, int wsPingPeriod, IBackoffStrategy connectionBackoffStrategy, int wsCloseDelay, Consumer<PubSubResponsePayload> fallbackTopicHandler) {
        this.eventManager = eventManager;
        this.taskExecutor = taskExecutor;
        this.botOwnerIds = botOwnerIds;

        if (fallbackTopicHandler != null) {
            this.fallbackTopicHandler = fallbackTopicHandler;
        } else {
            this.fallbackTopicHandler = message -> log.warn("Unparsable Message: " + message.getTopic() + "|" + message.getMessage());
        }

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

                // Handle Messages
                TopicHandler handler = HandlerRegistry.INSTANCE.getHandlers().get(topicName);
                boolean fallback = true;
                if (handler != null) {
                    TwitchEvent event = null;
                    try {
                        event = handler.apply(new TopicHandler.Args(topicParts, message.getData().getMessage(), botOwnerIds));
                    } catch (Exception e) {
                        log.warn("PubSub: Encountered exception when parsing message", e);
                    }

                    if (event != null) {
                        fallback = false;
                        try {
                            eventManager.publish(event);
                        } catch (Exception e) {
                            log.warn("An event consumer threw an exception while processing a PubSub event", e);
                        }
                    }
                }
                if (fallback) {
                    fallbackTopicHandler.accept(message.getData());
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
        // use latest token (in case of expiry)
        OAuth2Credential credential = request.getCredential();
        if (credential != null) {
            request.getData().put("auth_token", credential.getAccessToken());
        }

        // queue the request
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
