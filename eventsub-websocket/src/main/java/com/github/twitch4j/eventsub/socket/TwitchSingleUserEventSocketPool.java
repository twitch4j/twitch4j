package com.github.twitch4j.eventsub.socket;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.common.pool.TwitchModuleConnectionPool;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import io.github.xanthic.cache.api.Cache;
import io.github.xanthic.cache.api.domain.ExpiryType;
import io.github.xanthic.cache.core.CacheApi;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * A pool for a single user id to subscriptions spread over multiple EventSub websockets.
 */
@SuperBuilder
public final class TwitchSingleUserEventSocketPool extends TwitchModuleConnectionPool<TwitchEventSocket, EventSubSubscription, EventSubSubscription, Boolean, TwitchEventSocket.TwitchEventSocketBuilder> implements IEventSubSocket {

    private final String threadPrefix = "twitch4j-unitary-pool-" + CryptoUtils.generateNonce(4) + "-eventsub-ws-";

    /**
     * The base url for websocket connections.
     *
     * @see TwitchEventSocket#WEB_SOCKET_SERVER
     */
    @Builder.Default
    private String baseUrl = TwitchEventSocket.WEB_SOCKET_SERVER;

    /**
     * The {@link TwitchHelix} instance for creating eventsub subscriptions in the official API.
     */
    @Nullable
    @Builder.Default
    private TwitchHelix helix = TwitchHelixBuilder.builder().build();

    /**
     * The default credential (representing the single user) to create eventsub subscriptions with.
     */
    @Getter
    @Nullable
    private OAuth2Credential defaultToken;

    /**
     * A temporary cache of what credential is used for which eventsub subscription registration request,
     * so it can be delivered to the individual underlying socket to be used.
     */
    private final Cache<SubscriptionWrapper, OAuth2Credential> credentials = CacheApi.create(spec -> {
        spec.maxSize(Runtime.getRuntime().availableProcessors() * 4L);
        spec.expiryType(ExpiryType.POST_WRITE);
        spec.expiryTime(Duration.ofMinutes(5L));
    });

    @Override
    protected TwitchEventSocket createConnection() {
        if (closed.get()) throw new IllegalStateException("EventSocket cannot be created after pool was closed!");
        return advancedConfiguration.apply(
            TwitchEventSocket.builder()
                .api(helix)
                .baseUrl(baseUrl)
                .defaultToken(defaultToken)
                .eventManager(getConnectionEventManager())
                .proxyConfig(proxyConfig.get())
                .taskExecutor(getExecutor(threadPrefix + CryptoUtils.generateNonce(4), TwitchEventSocket.REQUIRED_THREAD_COUNT))
        ).build();
    }

    @Override
    @SneakyThrows
    protected void disposeConnection(TwitchEventSocket connection) {
        connection.close();
    }

    @Override
    protected EventSubSubscription handleSubscription(TwitchEventSocket twitchEventSocket, EventSubSubscription eventSubSubscription) {
        SubscriptionWrapper wrapped = SubscriptionWrapper.wrap(eventSubSubscription);
        OAuth2Credential cred = credentials.remove(wrapped);
        boolean success = twitchEventSocket.register(cred != null ? cred : defaultToken, wrapped);
        if (success) {
            return twitchEventSocket.getSubscriptions().stream()
                .filter(sub -> sub.equals(wrapped))
                .findAny()
                .orElse(wrapped);
        }
        return null;
    }

    @Override
    protected EventSubSubscription handleDuplicateSubscription(TwitchEventSocket twitchEventSocket, TwitchEventSocket old, EventSubSubscription eventSubSubscription) {
        return twitchEventSocket != null && twitchEventSocket != old && twitchEventSocket.unregister(eventSubSubscription) ? eventSubSubscription : null;
    }

    @Override
    protected Boolean handleUnsubscription(TwitchEventSocket twitchEventSocket, EventSubSubscription eventSubSubscription) {
        return twitchEventSocket != null && twitchEventSocket.unregister(eventSubSubscription);
    }

    @Override
    protected EventSubSubscription getRequestFromSubscription(EventSubSubscription eventSubSubscription) {
        return eventSubSubscription;
    }

    @Override
    protected int getSubscriptionSize(EventSubSubscription eventSubSubscription) {
        return 1;
    }

    @Override
    @Synchronized
    public void connect() {
        if (saturatedConnections.isEmpty() && unsaturatedConnections.isEmpty()) {
            unsaturatedConnections.put(createConnection(), 0);
        }
    }

    @Override
    public void disconnect() {
        getConnections().forEach(TwitchEventSocket::disconnect);
    }

    @Override
    public void reconnect() {
        getConnections().forEach(TwitchEventSocket::reconnect);
    }

    @Override
    public Collection<EventSubSubscription> getSubscriptions() {
        return Collections.unmodifiableSet(subscriptions.keySet());
    }

    @Override
    public boolean register(OAuth2Credential token, EventSubSubscription sub) {
        SubscriptionWrapper wrapped = SubscriptionWrapper.wrap(sub);
        credentials.put(wrapped, token != null ? token : Objects.requireNonNull(defaultToken));
        return subscribe(wrapped) != null;
    }

    @Override
    public boolean unregister(EventSubSubscription sub) {
        return this.unsubscribe(SubscriptionWrapper.wrap(sub));
    }

    @Override
    public long getLatency() {
        long sum = 0;
        int count = 0;
        for (TwitchEventSocket ws : getConnections()) {
            long latency = ws.getLatency();
            if (latency >= 0) {
                sum += latency;
                count++;
            }
        }
        return count > 0 ? sum / count : -1L;
    }
}
