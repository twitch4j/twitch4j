package com.github.twitch4j.eventsub.socket;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.common.pool.SubscriptionConnectionPool;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.common.util.EventManagerUtils;
import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;

/**
 * A pool for EventSub websocket subscriptions across multiple users.
 * <p>
 * Should <i>not</i> be used with the "conduit" transport type.
 */
@Slf4j
@Builder
public final class TwitchEventSocketPool implements IEventSubSocket {

    private final String threadPrefix = "twitch4j-multi-pool-" + CryptoUtils.generateNonce(4) + "-eventsub-ws-";

    /**
     * The default {@link EventManager} for this connection pool, if specified.
     */
    @Getter
    @Builder.Default
    private final EventManager eventManager = EventManagerUtils.initializeEventManager(SimpleEventHandler.class);

    /**
     * The {@link ScheduledThreadPoolExecutor} to be used by connections in this pool, if specified.
     */
    @Nullable
    private final ScheduledThreadPoolExecutor executor;

    /**
     * The {@link TwitchIdentityProvider} to enrich credentials.
     */
    @NotNull
    @Builder.Default
    private final TwitchIdentityProvider identityProvider = new TwitchIdentityProvider(null, null, null);

    /**
     * The default {@link OAuth2Credential} to use when initially creating an EventSub subscription.
     */
    @Nullable
    private final OAuth2Credential fallbackToken;

    /**
     * The base url for websocket connections.
     *
     * @see TwitchEventSocket#WEB_SOCKET_SERVER
     */
    @NotNull
    @Builder.Default
    private final String baseUrl = TwitchEventSocket.WEB_SOCKET_SERVER;

    /**
     * The {@link TwitchHelix} instance for creating eventsub subscriptions in the official API.
     */
    @Nullable
    @Builder.Default
    private TwitchHelix helix = TwitchHelixBuilder.builder().build();

    /**
     * The maximum number of eventsub subscriptions that a single user_id can have.
     */
    @Builder.Default
    private int maxSubscriptionsPerUser = TwitchEventSocket.MAX_SUBSCRIPTIONS_PER_SOCKET * 3; // imposed by twitch

    /**
     * Further configuration that should be applied to the builder when creating new EventSocket (single-user) pools.
     */
    @Builder.Default
    private final UnaryOperator<TwitchSingleUserEventSocketPool.TwitchSingleUserEventSocketPoolBuilder<?, ?>> advancedConfiguration = b -> b;

    /**
     * A mapping of user_id's to their individual eventsocket pools.
     */
    private final Map<String, TwitchSingleUserEventSocketPool> poolByUserId = new ConcurrentHashMap<>();

    /**
     * A mapping of eventsub subscriptions to which individual pool contains it.
     */
    private final Map<SubscriptionWrapper, TwitchSingleUserEventSocketPool> poolBySub = new ConcurrentHashMap<>();

    @Override
    public void connect() {
        // no-op
    }

    @Override
    public void disconnect() {
        poolByUserId.values().forEach(IEventSubSocket::disconnect);
    }

    @Override
    public void reconnect() {
        poolByUserId.values().forEach(IEventSubSocket::reconnect);
    }

    @Override
    @Synchronized
    public boolean register(OAuth2Credential credential, EventSubSubscription sub) {
        OAuth2Credential token = credential != null ? credential : getDefaultToken();
        if (token == null) return false;

        String userId = getUserId(token);
        if (userId == null) return false;

        SubscriptionWrapper wrapped = SubscriptionWrapper.wrap(sub);

        if (poolBySub.containsKey(wrapped))
            return false;

        TwitchSingleUserEventSocketPool pool = poolByUserId.computeIfAbsent(userId,
            id -> advancedConfiguration.apply(
                TwitchSingleUserEventSocketPool.builder()
                    .baseUrl(baseUrl)
                    .defaultToken(token)
                    .eventManager(eventManager)
                    .helix(helix)
                    .executor(() -> executor)
            ).build()
        );

        if (pool.numSubscriptions() >= maxSubscriptionsPerUser) {
            log.debug("Skipping eventsocket subscription registration because pool is already at capacity for user {}: {}", userId, sub);
            return false;
        }

        return pool.register(token, sub) && poolBySub.put(wrapped, pool) == null;
    }

    @Override
    @Synchronized
    public boolean unregister(EventSubSubscription sub) {
        SubscriptionWrapper wrapped = SubscriptionWrapper.wrap(sub);
        TwitchSingleUserEventSocketPool pool = poolBySub.get(wrapped);
        if (pool == null) return false;

        Boolean unsubscribe = pool.unsubscribe(wrapped);

        // cleanup if we removed the last subscription
        if (pool.numSubscriptions() <= 0) {
            poolByUserId.entrySet().stream()
                .filter(e -> e.getValue() == pool)
                .map(Map.Entry::getKey)
                .findAny()
                .ifPresent(userId -> {
                    AtomicBoolean close = new AtomicBoolean();

                    // noinspection resource
                    poolByUserId.computeIfPresent(userId, (k, v) -> {
                        if (v.numSubscriptions() <= 0) {
                            close.set(true);
                            return null; // remove mapping
                        }
                        return v;
                    });

                    if (close.get())
                        pool.close();
                });
        }

        // noinspection resource
        return unsubscribe != null && unsubscribe && poolBySub.remove(wrapped) != null;
    }

    @Override
    public Collection<EventSubSubscription> getSubscriptions() {
        return Collections.unmodifiableSet(poolBySub.keySet());
    }

    @Override
    @Synchronized
    public void close() throws Exception {
        poolBySub.clear();
        Collection<TwitchSingleUserEventSocketPool> pools = new LinkedList<>();
        poolByUserId.values().removeIf(pools::add);
        pools.forEach(SubscriptionConnectionPool::close);
    }

    @Nullable
    @Override
    public OAuth2Credential getDefaultToken() {
        return poolByUserId.values().stream()
            .filter(pool -> pool.getDefaultToken() != null)
            .min(Comparator.comparingInt(SubscriptionConnectionPool::numSubscriptions))
            .map(IEventSubSocket::getDefaultToken)
            .orElse(fallbackToken);
    }

    @Override
    public long getLatency() {
        long sum = 0;
        int count = 0;
        for (TwitchSingleUserEventSocketPool pool : poolByUserId.values()) {
            int n = pool.numConnections();
            long latency = pool.getLatency();
            if (latency >= 0) {
                sum += latency * n;
                count += n;
            }
        }
        return count > 0 ? sum / count : -1L;
    }

    /**
     * @return the number of open connections held by this pool.
     */
    public int numConnections() {
        int n = 0;
        for (TwitchSingleUserEventSocketPool pool : poolByUserId.values()) {
            n += pool.numConnections();
        }
        return n;
    }

    /**
     * @return the total number of subscriptions held by all connections.
     */
    public int numSubscriptions() {
        return getSubscriptions().size();
    }

    @Nullable
    private String getUserId(OAuth2Credential token) {
        if (StringUtils.isNotEmpty(token.getUserId())) return token.getUserId();
        identityProvider.getAdditionalCredentialInformation(token).ifPresent(token::updateCredential);
        return token.getUserId();
    }
}
