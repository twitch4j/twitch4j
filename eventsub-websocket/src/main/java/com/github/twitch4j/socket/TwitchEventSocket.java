package com.github.twitch4j.socket;

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
import com.github.twitch4j.eventsub.condition.EventSubCondition;
import com.github.twitch4j.eventsub.events.EventSubEvent;
import com.github.twitch4j.eventsub.util.EventSubVerifier;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.socket.domain.EventSubSocketInformation;
import com.github.twitch4j.socket.domain.EventSubSocketMessage;
import com.github.twitch4j.socket.domain.SocketCloseReason;
import com.github.twitch4j.socket.domain.SocketMessageMetadata;
import com.github.twitch4j.socket.domain.SocketPayload;
import com.github.twitch4j.socket.enums.SocketMessageType;
import io.github.xanthic.cache.api.Cache;
import io.github.xanthic.cache.core.CacheApi;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;

@Slf4j
public final class TwitchEventSocket implements IEventSubSocket {

    public static final int REQUIRED_THREAD_COUNT = 1;

    /**
     * Each WebSocket connection may create a maximum of 100 enabled subscriptions (disabled subscriptions don't count against the limit).
     */
    public static final int MAX_SUBSCRIPTIONS_PER_SOCKET = 100;

    /**
     * The WebSocket Server
     */
    public static final String WEB_SOCKET_SERVER = "wss://eventsub-beta.wss.twitch.tv/ws";

    static final BiPredicate<EventSubSubscription, EventSubSubscription> EQUALS = (a, b) -> (a.getId() != null && a.getId().equals(b.getId())) ||
        StringUtils.equals(a.getRawType(), b.getRawType()) && StringUtils.equals(a.getRawVersion(), b.getRawVersion()) && Objects.equals(a.getCondition(), b.getCondition());

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
     * WebSocket Connection
     */
    @NotNull
    private final AtomicReference<WebsocketConnection> connection;

    @NotNull
    private final AtomicReference<WebsocketConnection> expiringConnection = new AtomicReference<>();

    @NotNull
    private final BlockingQueue<EventSubSubscription> subscriptions = new LinkedBlockingQueue<>();

    @NotNull
    private final String baseUrl;

    @NotNull
    @Getter(AccessLevel.PRIVATE)
    private volatile String url;

    @Getter
    @Nullable
    private volatile String websocketId = null;

    private final Cache<Pair<Pair<String, String>, EventSubCondition>, OAuth2Credential> tokenByTopic;

    @Builder
    TwitchEventSocket(@Nullable String baseUrl, @Nullable String url, @Nullable String clientId, @Nullable String clientSecret, @Nullable EventManager eventManager, @Nullable ScheduledExecutorService taskExecutor,
                      @Nullable ProxyConfig proxyConfig, @Nullable WebsocketConnection connection, @Nullable TwitchHelix api, @Nullable OAuth2Credential defaultToken) {
        this.baseUrl = baseUrl != null ? baseUrl : WEB_SOCKET_SERVER;
        this.url = url != null ? url : this.baseUrl;
        this.eventManager = EventManagerUtils.validateOrInitializeEventManager(eventManager, SimpleEventHandler.class);
        this.executor = taskExecutor != null ? taskExecutor : Executors.newScheduledThreadPool(REQUIRED_THREAD_COUNT);
        this.proxyConfig = proxyConfig;
        this.defaultToken = defaultToken;

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
            if (conn == null) return buildConnection();
            if (StringUtils.equals(url, conn.getConfig().baseUrl())) return conn;
            if (expiringConnection.updateAndGet(c -> c == null ? conn : c) == conn) return buildConnection();
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
        getConnection().disconnect();
    }

    /**
     * Reconnecting to EventSub-WS
     */
    @Override
    public void reconnect() {
        this.websocketId = null;
        this.url = baseUrl;
        getConnection().reconnect();
    }

    @Override
    public void close() throws Exception {
        this.websocketId = null;

        WebsocketConnection current = connection.getAndSet(null);
        if (current != null)
            current.close();

        WebsocketConnection expiring = expiringConnection.getAndSet(null);
        if (expiring != null)
            expiring.close();

        // explicitly delete the attached subscriptions
        List<EventSubSubscription> subs = new ArrayList<>(subscriptions.size());
        subscriptions.drainTo(subs);
        executor.execute(() -> {
            subs.forEach(sub -> {
                if (StringUtils.isNotBlank(sub.getId())) {
                    try {
                        api.deleteEventSubSubscription(getAssociatedToken(sub), sub.getId()).execute();
                    } catch (Exception e) {
                        log.debug("Failed to delete event socket subscription on close: " + sub, e);
                    }
                }
            });

            tokenByTopic.clear();
        });
    }

    @Override
    @Synchronized
    public boolean register(OAuth2Credential token, EventSubSubscription sub) {
        if (subscriptions.stream().anyMatch(s -> EQUALS.test(s, sub)))
            return false; // avoid duplicates

        tokenByTopic.putIfAbsent(deriveKey(sub), token);

        final boolean alreadyEnabled = StringUtils.isNotBlank(sub.getId())
            && sub.getStatus() == EventSubSubscriptionStatus.ENABLED
            && StringUtils.equals(sub.getTransport().getSessionId(), websocketId);

        if (alreadyEnabled) {
            // user already manually called helix; add to our registry
            return subscriptions.offer(sub);
        } else {
            if (this.websocketId != null && getConnection().getConnectionState() == WebsocketConnectionState.CONNECTED) {
                // already connected => can immediately call helix to register
                return createSub(token, sub, augmentSub(sub, websocketId));
            } else {
                // we're not connected yet, defer until later
                return subscriptions.offer(sub);
            }
        }
    }

    @Override
    public boolean unregister(EventSubSubscription sub) {
        if (unsubscribeNoHelix(sub)) {
            if (StringUtils.isNotBlank(sub.getId())) {
                executor.execute(() -> {
                    try {
                        api.deleteEventSubSubscription(getAuthToken(defaultToken), sub.getId()).execute();
                    } catch (Exception e) {
                        log.warn("Failed to delete EventSub-WS subscription via Twitch API {}", sub, e);
                    }
                });
            }
            return true;
        }
        return false;
    }

    @Override
    public Collection<EventSubSubscription> getSubscriptions() {
        return Collections.unmodifiableCollection(this.subscriptions);
    }

    public WebsocketConnectionState getState() {
        WebsocketConnection ws = connection.get();
        return ws != null ? ws.getConnectionState() : WebsocketConnectionState.DISCONNECTED;
    }

    @Synchronized
    private boolean unsubscribeNoHelix(EventSubSubscription remove) {
        return subscriptions.removeIf(sub -> EQUALS.test(sub, remove));
    }

    private void onInitialConnection(final String websocketId) {
        final List<EventSubSubscription> oldSubs = new ArrayList<>(subscriptions.size());
        subscriptions.drainTo(oldSubs);

        for (final EventSubSubscription old : oldSubs) {
            if (StringUtils.equals(old.getTransport().getSessionId(), websocketId)) {
                // this branch shouldn't be hit, but is a performance optimization to avoid helix
                subscriptions.add(old);
            } else {
                executor.execute(() -> {
                    final OAuth2Credential credential = getAssociatedCredential(old);
                    final EventSubSubscription newSub = augmentSub(old, websocketId);

                    if (StringUtils.isNotEmpty(old.getId())) {
                        try {
                            api.deleteEventSubSubscription(getAuthToken(credential), old.getId()).execute();
                            log.trace("EventSub-WS deleted subscription {}", old);
                        } catch (Exception e) {
                            log.debug("Could not delete old EventSub-WS subscription {}", old, e);
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
                this.websocketId = socket.getId();

                log.debug("EventSub-WS connection was welcomed at {}", socket.getConnectedAt());

                if (baseUrl.equals(url)) {
                    // If this is not a reconnect, let's create the eventsub subscriptions
                    this.onInitialConnection(socket.getId());
                } else {
                    // noinspection deprecation
                    subscriptions.forEach(sub -> sub.getTransport().setSessionId(websocketId));
                }

                // For reconnects, we can close the old connection now
                WebsocketConnection expired = expiringConnection.getAndSet(null);
                attemptClose(expired);

                // Post-reconnect we should use the standard wss url going forward
                this.url = baseUrl;
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
                if (unsubscribeNoHelix(revoked)) {
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

    private boolean createSub(OAuth2Credential token, EventSubSubscription oldSub, EventSubSubscription newSub) {
        try {
            EventSubSubscription sub = api.createEventSubSubscription(getAuthToken(token), newSub).execute().getSubscriptions().get(0);
            subscriptions.add(Objects.requireNonNull(sub));
            log.debug("EventSub-WS successfully created subscription {}", sub);
            tokenByTopic.put(deriveKey(sub), token);
            return true;
        } catch (Exception e) {
            log.error("Failed to create EventSub-WS subscription {}", newSub, e);
            // try again on next reconnect
            subscriptions.offer(
                oldSub.toBuilder()
                    .status(null)
                    .createdAt(null)
                    .transport(oldSub.getTransport().withSessionId(null))
                    .build()
            );
            return false;
        }
    }

    private WebsocketConnection getConnection() {
        return connection.updateAndGet(conn -> conn != null ? conn : buildConnection());
    }

    private WebsocketConnection buildConnection() {
        return new WebsocketConnection(spec -> {
            spec.baseUrl(url);
            spec.taskExecutor(executor);
            spec.onTextMessage(this::onTextMessage);
            spec.onStateChanged((oldState, newState) -> {
                if (newState == WebsocketConnectionState.LOST && spec.baseUrl().equals(this.getUrl())) {
                    this.websocketId = null;

                    // noinspection deprecation
                    subscriptions.forEach(sub -> sub.setStatus(EventSubSubscriptionStatus.WEBSOCKET_NETWORK_TIMEOUT));
                }
            });
            spec.onCloseFrame(data -> {
                SocketCloseReason reason = null;
                try {
                    int code = Integer.parseInt(Objects.requireNonNull(data));
                    reason = Objects.requireNonNull(SocketCloseReason.MAPPINGS.get(code));
                } catch (Exception e) {
                    log.warn("Failed to parse eventsub websocket close frame reason: " + data, e);
                }

                log.debug("Twitch disconnected the EventSub-WS connection {} because {}", websocketId, reason);
                executor.execute(this::connect);
            });
            if (proxyConfig != null) spec.proxyConfig(proxyConfig);
        });
    }

    private OAuth2Credential getAssociatedCredential(EventSubSubscription sub) {
        OAuth2Credential associated = tokenByTopic.get(deriveKey(sub));
        return associated != null ? associated : defaultToken;
    }

    private String getAssociatedToken(EventSubSubscription sub) {
        return getAuthToken(getAssociatedCredential(sub));
    }

    static String getAuthToken(OAuth2Credential token) {
        return token != null ? token.getAccessToken() : null;
    }

    private static Pair<Pair<String, String>, EventSubCondition> deriveKey(@NotNull EventSubSubscription sub) {
        return Pair.of(Pair.of(sub.getRawType(), sub.getRawVersion()), sub.getCondition());
    }

    private static EventSubSubscription augmentSub(EventSubSubscription old, String newWebSocketId) {
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
