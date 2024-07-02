package com.github.twitch4j.eventsub.socket;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.client.websocket.WebsocketConnection;
import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.util.EventManagerUtils;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.EventSubSubscriptionStatus;
import com.github.twitch4j.eventsub.EventSubTransport;
import com.github.twitch4j.eventsub.EventSubTransportMethod;
import com.github.twitch4j.eventsub.events.EventSubEvent;
import com.github.twitch4j.eventsub.socket.domain.EventSubSocketInformation;
import com.github.twitch4j.eventsub.socket.domain.EventSubSocketMessage;
import com.github.twitch4j.eventsub.socket.domain.SocketCloseReason;
import com.github.twitch4j.eventsub.socket.domain.SocketMessageMetadata;
import com.github.twitch4j.eventsub.socket.enums.SocketMessageType;
import com.github.twitch4j.eventsub.socket.events.EventSocketClosedByTwitchEvent;
import com.github.twitch4j.eventsub.socket.events.EventSocketConnectionStateEvent;
import com.github.twitch4j.eventsub.socket.events.EventSocketDeleteSubscriptionFailureEvent;
import com.github.twitch4j.eventsub.socket.events.EventSocketDeleteSubscriptionSuccessEvent;
import com.github.twitch4j.eventsub.socket.events.EventSocketSubscriptionFailureEvent;
import com.github.twitch4j.eventsub.socket.events.EventSocketSubscriptionSuccessEvent;
import com.github.twitch4j.eventsub.socket.events.EventSocketWelcomedEvent;
import com.github.twitch4j.eventsub.util.EventSubVerifier;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.eventsub.socket.domain.SocketPayload;
import com.github.twitch4j.util.IBackoffStrategy;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import io.github.xanthic.cache.api.Cache;
import io.github.xanthic.cache.core.CacheApi;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A single EventSub websocket for a single user id.
 */
@Slf4j
public final class TwitchEventSocket implements IEventSubSocket {

    public static final int REQUIRED_THREAD_COUNT = 1;

    /**
     * Each WebSocket connection may create a maximum of 300 enabled subscriptions (disabled subscriptions don't count against the limit).
     */
    public static final int MAX_SUBSCRIPTIONS_PER_SOCKET = 300;

    /**
     * The WebSocket Server
     */
    public static final String WEB_SOCKET_SERVER = "wss://eventsub.wss.twitch.tv/ws";

    /**
     * Helix API instance
     */
    @NotNull
    private final TwitchHelix api;

    /**
     * Default Helix Token
     */
    @Getter
    @Nullable
    private final OAuth2Credential defaultToken;

    /**
     * EventManager
     */
    @Getter
    @NotNull
    private final EventManager eventManager;

    /**
     * Thread Pool Executor
     */
    @NotNull
    private final ScheduledExecutorService executor;

    /**
     * Proxy Configuration
     */
    @Nullable
    private final ProxyConfig proxyConfig;

    /**
     * Backoff Strategy Configuration
     */
    @Nullable
    private final IBackoffStrategy backoffStrategy;

    /**
     * Whether subscriptions that fail creation due to a 4xx error should be retried on next reconnect.
     */
    private final boolean avoidRetryFailedSubscription;

    /**
     * WebSocket Connection
     */
    @NotNull
    private final AtomicReference<WebsocketConnection> connection;

    /**
     * The previous websocket connection that is temporarily held when Twitch directs us to reconnect to a different edge server.
     */
    @NotNull
    private final AtomicReference<@Nullable WebsocketConnection> expiringConnection = new AtomicReference<>();

    /**
     * The eventsub subscriptions tracked by this socket (though all may not be successfully registered with the API).
     * <p>
     * The subscription underlying the key is exactly the same as the value.
     * The key just avoids duplicating subscriptions (based on type/version and condition).
     */
    @NotNull
    private final Map<SubscriptionWrapper, EventSubSubscription> subscriptions = new ConcurrentHashMap<>();

    /**
     * The base url for websocket connections.
     *
     * @see TwitchEventSocket#WEB_SOCKET_SERVER
     */
    @NotNull
    private final String baseUrl;

    /**
     * The current url the websocket connection is pointed to.
     */
    @NotNull
    @Getter(AccessLevel.PRIVATE)
    private volatile String url;

    /**
     * The Twitch-assigned id associated with this websocket,
     * which is used when creating eventsub subscriptions via the API.
     */
    @Getter
    @Nullable
    private volatile String websocketId = null;

    /**
     * The token associated with each eventsub subscription (in order to manage the subscription via helix).
     */
    private final Cache<SubscriptionWrapper, OAuth2Credential> tokenByTopic;

    @Builder
    TwitchEventSocket(@Nullable String baseUrl, @Nullable String url, @Nullable String clientId, @Nullable String clientSecret, @Nullable EventManager eventManager, @Nullable ScheduledExecutorService taskExecutor, @Nullable ProxyConfig proxyConfig,
                      @Nullable WebsocketConnection connection, @Nullable TwitchHelix api, @Nullable OAuth2Credential defaultToken, @Nullable IBackoffStrategy backoffStrategy, @Nullable Boolean avoidRetryFailedSubscription) {
        this.baseUrl = baseUrl != null ? baseUrl : WEB_SOCKET_SERVER;
        this.url = url != null ? url : this.baseUrl;
        this.eventManager = EventManagerUtils.validateOrInitializeEventManager(eventManager, SimpleEventHandler.class);
        this.executor = taskExecutor != null ? taskExecutor : Executors.newScheduledThreadPool(REQUIRED_THREAD_COUNT);
        this.proxyConfig = proxyConfig;
        this.defaultToken = defaultToken;
        this.backoffStrategy = backoffStrategy;
        this.avoidRetryFailedSubscription = avoidRetryFailedSubscription != null ? avoidRetryFailedSubscription : true;

        // init token map
        this.tokenByTopic = CacheApi.create(spec -> {
            spec.executor(executor);
            spec.maxSize(MAX_SUBSCRIPTIONS_PER_SOCKET * 4L);
        });

        // init connection
        if (connection == null) {
            this.connection = new AtomicReference<>(this.buildConnection());
        } else {
            this.connection = new AtomicReference<>(connection);
        }

        // init api
        if (api == null) {
            if (defaultToken == null || StringUtils.isBlank(defaultToken.getAccessToken())) {
                log.warn("EventSub Websockets currently requires a user access token, which has not been passed!");
            }

            this.api = TwitchHelixBuilder.builder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withDefaultAuthToken(defaultToken)
                .build();
        } else {
            this.api = api;
        }

        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j-eventsub-websocket", this);

        // connect
        this.connect();
    }

    /**
     * Connecting to EventSub-WS
     */
    @Override
    @SuppressWarnings("resource")
    public void connect() {
        WebsocketConnection socket = connection.updateAndGet(conn -> {
            // No connection outstanding, build a new one
            if (conn == null)
                return buildConnection();

            // Current connection is pointed at wrong url, build a new one
            if (!Objects.equals(url, conn.getConfig().baseUrl())) {
                executor.execute(() -> attemptClose(conn)); // avoid resource leak
                return buildConnection();
            }

            // Current connection is expiring, build a new one
            if (conn == expiringConnection.get()) {
                return buildConnection();
            }

            // Otherwise, existing connection is appropriate to continue using
            return conn;
        });
        socket.connect();
    }

    /**
     * Disconnecting from EventSub-WS
     */
    @Override
    public void disconnect() {
        this.websocketId = null;
        this.url = baseUrl;

        WebsocketConnection socket = connection.get();
        if (socket != null)
            socket.disconnect();
    }

    /**
     * Reconnecting to EventSub-WS
     */
    @Override
    public void reconnect() {
        this.disconnect();
        this.connect();
    }

    @Override
    @Synchronized
    public void close() throws Exception {
        this.websocketId = null;

        WebsocketConnection current = connection.getAndSet(null);
        if (current != null)
            current.close();

        WebsocketConnection expiring = expiringConnection.getAndSet(null);
        if (expiring != null)
            expiring.close();

        // explicitly delete the attached subscriptions
        Collection<Future<?>> futures = new ArrayDeque<>(subscriptions.size());
        subscriptions.keySet().removeIf(sub -> futures.add(
            executor.submit(() -> {
                if (StringUtils.isNotBlank(sub.getId())) {
                    try {
                        api.deleteEventSubSubscription(getAssociatedToken(sub), sub.getId()).execute();
                        eventManager.publish(new EventSocketDeleteSubscriptionSuccessEvent(sub, this));
                    } catch (Exception e) {
                        log.debug("Failed to delete event socket subscription on close: " + sub, e);
                        eventManager.publish(new EventSocketDeleteSubscriptionFailureEvent(sub, this, e));
                    }
                }
            })
        ));

        // wait for delete calls to complete
        for (Future<?> future : futures) {
            future.get();
        }

        tokenByTopic.clear();
        futures.clear();
        subscriptions.clear();
    }

    @Override
    @Synchronized
    public boolean register(OAuth2Credential token, EventSubSubscription sub) {
        SubscriptionWrapper wrapped = SubscriptionWrapper.wrap(sub);
        if (subscriptions.containsKey(wrapped))
            return false; // avoid duplicates

        // ensure transport is not null
        EventSubTransportMethod method = sub.getTransport() != null ? sub.getTransport().getMethod() : null;
        if (method != EventSubTransportMethod.WEBSOCKET && method != EventSubTransportMethod.CONDUIT) {
            // noinspection deprecation
            sub.setTransport(EventSubTransport.builder().method(EventSubTransportMethod.WEBSOCKET).build());
        }

        if (token != null)
            tokenByTopic.putIfAbsent(wrapped, token);

        final boolean alreadyEnabled = StringUtils.isNotBlank(sub.getTransport().getConduitId()) ||
            (StringUtils.isNotBlank(sub.getId())
                && sub.getStatus() == EventSubSubscriptionStatus.ENABLED
                && Objects.equals(sub.getTransport().getSessionId(), websocketId));

        if (alreadyEnabled) {
            // user already manually called helix; add to our registry
            return subscriptions.putIfAbsent(wrapped, wrapped) == null;
        } else {
            if (this.websocketId != null && getConnection().getConnectionState() == WebsocketConnectionState.CONNECTED) {
                // already connected => can immediately call helix to register
                return createSub(token, sub, augmentSub(sub, websocketId));
            } else {
                // we're not connected yet, defer until later
                return subscriptions.putIfAbsent(wrapped, wrapped) == null;
            }
        }
    }

    @Override
    @Synchronized
    public boolean unregister(EventSubSubscription subscription) {
        EventSubSubscription sub = unsubscribeNoHelix(subscription);
        if (sub != null) {
            if (StringUtils.isNotBlank(sub.getId())) {
                executor.execute(() -> {
                    try {
                        api.deleteEventSubSubscription(getAssociatedToken(sub), sub.getId()).execute();
                        eventManager.publish(new EventSocketDeleteSubscriptionSuccessEvent(sub, this));
                    } catch (Exception e) {
                        log.warn("Failed to delete EventSub-WS subscription via Twitch API {}", sub, e);
                        eventManager.publish(new EventSocketDeleteSubscriptionFailureEvent(sub, this, e));
                    }
                });
            }
            return true;
        }
        return false;
    }

    @Override
    public Collection<EventSubSubscription> getSubscriptions() {
        return Collections.unmodifiableSet(this.subscriptions.keySet());
    }

    @Override
    public long getLatency() {
        WebsocketConnection ws = connection.get();
        return ws != null ? ws.getLatency() : -1L;
    }

    public WebsocketConnectionState getState() {
        WebsocketConnection ws = connection.get();
        return ws != null ? ws.getConnectionState() : WebsocketConnectionState.DISCONNECTED;
    }

    @Nullable
    private EventSubSubscription unsubscribeNoHelix(@NotNull EventSubSubscription remove) {
        return subscriptions.remove(SubscriptionWrapper.wrap(remove));
    }

    @Synchronized
    private void onInitialConnection(@NotNull final String websocketId) {
        final Collection<EventSubSubscription> oldSubs = new ArrayDeque<>(subscriptions.size());
        subscriptions.keySet().removeIf(oldSubs::add);

        for (final EventSubSubscription old : oldSubs) {
            EventSubTransport transport = old.getTransport();
            if (websocketId.equals(transport.getSessionId()) || StringUtils.isNotBlank(transport.getConduitId())) {
                subscriptions.putIfAbsent(SubscriptionWrapper.wrap(old), old);
            } else {
                executor.execute(() -> {
                    final OAuth2Credential credential = getAssociatedCredential(old);
                    final EventSubSubscription newSub = augmentSub(old, websocketId);

                    // clean up subscription pointing at another websocket id (likely dead)
                    if (StringUtils.isNotEmpty(old.getId())) {
                        try {
                            api.deleteEventSubSubscription(getAuthToken(credential), old.getId()).execute();
                            log.trace("EventSub-WS deleted subscription {}", old);
                            eventManager.publish(new EventSocketDeleteSubscriptionSuccessEvent(old, this));
                        } catch (Exception e) {
                            log.debug("Could not delete old EventSub-WS subscription {}", old, e);
                            eventManager.publish(new EventSocketDeleteSubscriptionFailureEvent(old, this, e));
                        }
                    }

                    createSub(credential, old, newSub);
                });
            }
        }
    }

    private void onTextMessage(final String jsonMessage) {
        log.trace("Received EventSub-WS message: {}", jsonMessage);

        // Parse message
        final EventSubSocketMessage message;
        final SocketMessageMetadata metadata;
        final SocketMessageType messageType;
        final SocketPayload payload;
        try {
            message = TypeConvert.jsonToObject(jsonMessage, EventSubSocketMessage.class);
            metadata = Objects.requireNonNull(message.getMetadata());
            messageType = Objects.requireNonNull(metadata.getMessageType());
            payload = message.getPayload();

            if (messageType == SocketMessageType.SESSION_WELCOME) {
                Objects.requireNonNull(payload);
                Objects.requireNonNull(payload.getSession());
                Objects.requireNonNull(payload.getSession().getId());
            } else if (messageType == SocketMessageType.SESSION_RECONNECT) {
                Objects.requireNonNull(payload.getSession());
            } else if (messageType == SocketMessageType.NOTIFICATION) {
                Objects.requireNonNull(metadata.getSubscriptionType());
                Objects.requireNonNull(metadata.getSubscriptionVersion());
            } else if (messageType == SocketMessageType.REVOCATION) {
                Objects.requireNonNull(payload.getSubscription());
            }
        } catch (Exception e) {
            log.error("Failed to parse EventSub-WS message", e);
            return;
        }

        // Ignore duplicate messages
        if (!EventSubVerifier.verifyMessageId(metadata.getMessageId())) {
            log.debug("EventSub-WS received (and ignored) duplicate message {}", message);
            return;
        }

        // Handle message by type
        switch (messageType) {
            case SESSION_WELCOME:
                EventSubSocketInformation socket = payload.getSession();
                final boolean sessionChanged = !socket.getId().equals(this.websocketId);
                this.websocketId = socket.getId();

                log.debug("EventSub-WS connection was welcomed at {}", socket.getConnectedAt());

                if (sessionChanged) {
                    if (baseUrl.equals(url)) {
                        // If this is not a reconnect, let's create the eventsub subscriptions
                        this.onInitialConnection(socket.getId());
                    } else {
                        // this branch is never hit because Twitch reuses the session ID after requested reconnects
                        subscriptions.values().forEach(sub -> {
                            if (StringUtils.isBlank(sub.getTransport().getConduitId())) {
                                // noinspection deprecation
                                sub.getTransport().setSessionId(websocketId);
                            }
                        });
                    }
                }

                // For reconnects, we can close the old connection now
                WebsocketConnection expired = expiringConnection.getAndSet(null);
                attemptClose(expired);

                // Post-reconnect we should use the standard wss url going forward
                this.url = baseUrl;

                // fire meta event
                eventManager.publish(new EventSocketWelcomedEvent(this, socket.getId(), sessionChanged));
                break;

            case SESSION_KEEPALIVE:
                log.trace("EventSub-WS connection received keep alive message");
                break;

            case SESSION_RECONNECT:
                final String reconnectUrl = payload.getSession().getReconnectUrl();
                if (StringUtils.isBlank(reconnectUrl)) {
                    log.warn("EventSub-WS was unable to parse url in reconnect message: {}", jsonMessage);
                    reconnect();
                    return;
                }

                WebsocketConnection old = getConnection();
                expiringConnection.set(old);
                executor.schedule(() -> {
                    // Close the socket ourselves if the grace period expires and no welcome has been received on the new socket
                    if (expiringConnection.compareAndSet(old, null)) {
                        attemptClose(old);
                    }
                }, 30, TimeUnit.SECONDS);

                this.url = reconnectUrl;
                this.connect(); // will use the latest url
                break;

            case NOTIFICATION:
                final EventSubEvent event;
                try {
                    event = Objects.requireNonNull(payload.getParsedEvent());
                } catch (Exception e) {
                    log.error("EventSub-WS failed to parse notification event data: " + payload.getEventData(), e);
                    return;
                }

                try {
                    eventManager.publish(event);
                } catch (Exception e) {
                    log.error("Non-T4J event consumer threw exception while handling EventSocket notification: " + event, e);
                }
                break;

            case REVOCATION:
                final EventSubSubscription revoked = payload.getSubscription();
                if (unsubscribeNoHelix(revoked) != null) {
                    log.debug("Removed revoked EventSub-WS subscription {}", revoked);

                    try {
                        eventManager.publish(payload.getSubscription());
                    } catch (Exception e) {
                        log.error("Non-T4J event consumer threw exception while handling revoked EventSocket subscription", e);
                    }
                } else {
                    log.warn("Failed to identify revoked EventSub-WS subscription {}", revoked);
                }
                break;
        }
    }

    private boolean createSub(OAuth2Credential credential, EventSubSubscription oldSub, EventSubSubscription newSub) {
        OAuth2Credential token = credential != null ? credential : defaultToken;
        try {
            SubscriptionWrapper sub = SubscriptionWrapper.wrap(
                api.createEventSubSubscription(getAuthToken(token), newSub)
                    .execute()
                    .getSubscriptions()
                    .get(0)
            );
            subscriptions.put(sub, sub);
            log.debug("EventSub-WS successfully created subscription {}", sub);
            if (token != null) tokenByTopic.put(sub, token);
            eventManager.publish(new EventSocketSubscriptionSuccessEvent(sub, this));
            return true;
        } catch (Exception e) {
            log.error("Failed to create EventSub-WS subscription {}", newSub, e);

            // skip retry on confirmed bad subscriptions
            boolean retry = true;
            if (avoidRetryFailedSubscription && e instanceof HystrixRuntimeException && e.getCause() instanceof ContextedRuntimeException) {
                ContextedRuntimeException cause = (ContextedRuntimeException) e.getCause();
                String status = String.valueOf(cause.getFirstContextValue("errorStatus"));
                try {
                    int code = Integer.parseInt(status);
                    if (code >= 400 && code < 500 && code != 429) {
                        retry = false;
                    }
                } catch (NumberFormatException ignored) {
                }
            }

            // try again on next reconnect
            if (retry) {
                SubscriptionWrapper later = SubscriptionWrapper.wrap(
                    oldSub.toBuilder()
                        .status(null)
                        .createdAt(null)
                        .transport(oldSub.getTransport().withSessionId(null))
                        .build()
                );
                subscriptions.putIfAbsent(later, later);
            } else {
                log.warn("Will not retry subscription due to creation failure: {}", newSub);
            }

            // fire meta event
            eventManager.publish(new EventSocketSubscriptionFailureEvent(newSub, this, e, retry));
            return false;
        }
    }

    private WebsocketConnection getConnection() {
        return connection.updateAndGet(conn -> conn != null ? conn : buildConnection());
    }

    private WebsocketConnection buildConnection() {
        AtomicReference<WebsocketConnection> wsRef = new AtomicReference<>();
        WebsocketConnection ws = new WebsocketConnection(spec -> {
            spec.baseUrl(url);
            spec.taskExecutor(executor);
            spec.wsPingPeriod(15_000);
            spec.onTextMessage(this::onTextMessage);
            spec.onStateChanged((oldState, newState) -> {
                WebsocketConnection thisSocket = wsRef.get();
                if (thisSocket != null && thisSocket == connection.get()) {
                    boolean lost = newState == WebsocketConnectionState.LOST;

                    if (lost) {
                        this.websocketId = null;

                        // noinspection deprecation
                        subscriptions.values().forEach(sub -> sub.setStatus(EventSubSubscriptionStatus.WEBSOCKET_NETWORK_TIMEOUT));
                    }

                    eventManager.publish(new EventSocketConnectionStateEvent(oldState, newState, this));

                    // If connection is lost, make sure outstanding socket points to base url
                    if (lost && !spec.baseUrl().equals(this.getUrl())) {
                        wsRef.lazySet(null);
                        executor.execute(this::reconnect); // will create new socket at correct url
                        thisSocket.disconnect();
                    }
                }
            });
            spec.onCloseFrame((code, payload) -> {
                SocketCloseReason reason = SocketCloseReason.MAPPINGS.get(code);
                if (reason == null) {
                    log.warn("Failed to parse eventsub websocket close frame payload: {} = {}", code, payload);
                } else {
                    log.debug("Twitch disconnected the EventSub-WS connection {} because {}", websocketId, reason);
                }

                if (reason == SocketCloseReason.RECONNECT_GRACE_TIME_EXPIRED) {
                    // ignore events from expiring connection
                    return;
                }

                if (reason == SocketCloseReason.INVALID_RECONNECT) {
                    // avoid bad reconnect url
                    this.url = baseUrl;
                }

                if (reason == SocketCloseReason.CONNECTION_UNUSED && subscriptions.isEmpty()) {
                    // avoid infinite reconnect loop
                    executor.execute(this::disconnect);
                } else {
                    executor.execute(this::connect);
                }

                // fire meta event
                if (wsRef.get() == connection.get())
                    eventManager.publish(new EventSocketClosedByTwitchEvent(reason, this));
            });
            if (proxyConfig != null) spec.proxyConfig(proxyConfig);
            if (backoffStrategy != null) spec.backoffStrategy(backoffStrategy);
        });
        wsRef.set(ws);
        return ws;
    }

    private OAuth2Credential getAssociatedCredential(EventSubSubscription sub) {
        OAuth2Credential associated = tokenByTopic.get(SubscriptionWrapper.wrap(sub));
        return associated != null ? associated : defaultToken;
    }

    private String getAssociatedToken(EventSubSubscription sub) {
        return getAuthToken(getAssociatedCredential(sub));
    }

    static String getAuthToken(OAuth2Credential token) {
        return token != null ? token.getAccessToken() : null;
    }

    private static EventSubSubscription augmentSub(EventSubSubscription old, String newWebSocketId) {
        assert old.getTransport() != null;
        return EventSubSubscription.builder()
            .type(old.getType())
            .condition(old.getCondition())
            .transport(old.getTransport().withSessionId(newWebSocketId))
            .isBatchingEnabled(old.isBatchingEnabled())
            .rawType(old.getRawType())
            .rawVersion(old.getRawVersion())
            .build();
    }

    private static void attemptClose(WebsocketConnection connection) {
        if (connection == null) return;
        try {
            connection.close();
        } catch (Exception e) {
            log.warn("Failed to close old EventSub-WS connection", e);

            // just attempt simple disconnect to avoid a resource leak
            try {
                connection.disconnect();
            } catch (Exception ignored) {
            }
        }
    }

}
