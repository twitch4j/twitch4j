package com.github.twitch4j.eventsub.socket;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.util.EventManagerUtils;
import com.github.twitch4j.common.util.ThreadUtils;
import com.github.twitch4j.eventsub.Conduit;
import com.github.twitch4j.eventsub.ConduitShard;
import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.EventSubSubscriptionStatus;
import com.github.twitch4j.eventsub.EventSubTransport;
import com.github.twitch4j.eventsub.EventSubTransportMethod;
import com.github.twitch4j.eventsub.condition.EventSubCondition;
import com.github.twitch4j.eventsub.socket.events.EventSocketWelcomedEvent;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.helix.domain.ShardsInput;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Facilitates creating EventSub WebSocket shards for a new or existing Conduit.
 * <p>
 * Sample usage:
 * <pre>
 * {@code
 * IEventSubConduit conduit = TwitchConduitSocketPool.builder()
 *     .clientId("your-client-id")
 *     .clientSecret("your-client-secret")
 *     .poolShards(4) // customizable pool size
 *     .build();
 * conduit.register(SubscriptionTypes.STREAM_ONLINE, b -> b.broadcasterUserId("71092938").build());
 * conduit.getEventManager().onEvent(StreamOnlineEvent.class, System.out::println);
 * }
 * </pre>
 *
 * @see <a href="https://discuss.dev.twitch.com/t/available-today-twitch-chat-on-eventsub-an-api-for-sending-chat-and-the-conduit-transport-method-for-eventsub/54596#introducing-conduits-an-eventsub-transport-method-for-scale-4">Conduits Announcement</a>
 * @see <a href="https://dev.twitch.tv/docs/eventsub/handling-conduit-events/">Official Documentation</a>
 */
@Slf4j
public final class TwitchConduitSocketPool implements IEventSubConduit {

    @NotNull
    private final TwitchHelix api;

    @NotNull
    private final ScheduledThreadPoolExecutor executor;

    private final boolean shouldCloseExecutor;

    @Getter
    @NotNull
    private final EventManager eventManager;

    @Nullable
    private final OAuth2Credential credential;

    @Getter
    private final int shardOffset;

    @Getter
    @NotNull
    private final String conduitId;

    private final boolean shouldDeleteConduit;

    @NotNull
    private final EventSubTransport transport;

    private final List<TwitchEventSocket> sockets;

    @Builder
    @SneakyThrows
    TwitchConduitSocketPool(@Nullable String conduitId, @Nullable Integer totalShardCount, int poolShards, int shardOffset, @Nullable String clientId, @Nullable String clientSecret, @Nullable TwitchHelix helix, @Nullable OAuth2Credential appAccessToken, @Nullable ScheduledThreadPoolExecutor executor, @Nullable EventManager eventManager, @Nullable ProxyConfig proxyConfig, @Nullable Duration socketWelcomeTimeout) {
        if (poolShards <= 0) throw new IllegalArgumentException("Invalid number of pool shards");
        if (shardOffset < 0) throw new IllegalArgumentException("Invalid shard offset");
        if (conduitId == null && totalShardCount != null && totalShardCount < poolShards)
            throw new IllegalArgumentException("Cannot create more sockets than total shards");
        if (appAccessToken == null && (clientId == null || clientSecret == null) && helix == null && conduitId == null)
            throw new IllegalArgumentException("Conduit pool is missing authorization");

        this.credential = appAccessToken;
        this.eventManager = EventManagerUtils.validateOrInitializeEventManager(eventManager, SimpleEventHandler.class);
        this.shardOffset = shardOffset;

        if (executor == null) {
            String threadPrefix = "twitch4j-conduit-pool-" + RandomStringUtils.random(4, true, true) + "-eventsub-ws-";
            this.executor = ThreadUtils.getDefaultScheduledThreadPoolExecutor(threadPrefix, Runtime.getRuntime().availableProcessors());
            this.shouldCloseExecutor = true;
        } else {
            this.executor = executor;
            this.shouldCloseExecutor = false;
        }

        if (helix == null) {
            this.api = TwitchHelixBuilder.builder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withDefaultAuthToken(credential)
                .withProxyConfig(proxyConfig)
                .withScheduledThreadPoolExecutor(this.executor)
                .build();
        } else {
            this.api = helix;
        }

        // Create conduit if it does not exist
        String token = appAccessToken != null ? appAccessToken.getAccessToken() : null;
        if (conduitId == null) {
            int totalShards = totalShardCount != null ? totalShardCount : poolShards;
            this.shouldDeleteConduit = poolShards >= totalShards;
            try {
                this.conduitId = api.createConduit(token, totalShards).execute().getConduits().get(0).getId();
            } catch (Exception e) {
                if (shouldCloseExecutor) {
                    this.executor.shutdownNow();
                }
                throw new RuntimeException("Failed to create Conduit for pool", e);
            }
        } else {
            this.conduitId = conduitId;
            this.shouldDeleteConduit = false;

            int totalShards;
            if (totalShardCount != null) {
                totalShards = totalShardCount;
            } else {
                try {
                    // noinspection OptionalGetWithoutIsPresent
                    totalShards = api.getConduits(token).execute()
                        .getConduits()
                        .stream()
                        .filter(c -> conduitId.equals(c.getId()))
                        .mapToInt(Conduit::getShardCount)
                        .findAny()
                        .getAsInt();
                } catch (Exception e) {
                    if (shouldCloseExecutor) {
                        this.executor.shutdownNow();
                    }
                    throw new RuntimeException("Could not find existing Conduit for pool with ID: " + conduitId, e);
                }
            }

            int requiredShards = shardOffset + poolShards;
            if (requiredShards > totalShards) {
                try {
                    api.updateConduit(token, conduitId, requiredShards).execute();
                } catch (Exception e) {
                    if (shouldCloseExecutor) {
                        this.executor.shutdownNow();
                    }
                    throw new RuntimeException("Failed to expand size of Conduit with ID: " + conduitId, e);
                }
            }
        }
        this.transport = EventSubTransport.builder()
            .method(EventSubTransportMethod.CONDUIT)
            .conduitId(this.conduitId)
            .build();

        // Spawn websockets
        this.sockets = new ArrayList<>(poolShards);
        Set<TwitchEventSocket> set = Collections.synchronizedSet(new HashSet<>(poolShards));
        CountDownLatch latch = new CountDownLatch(poolShards);
        IDisposable welcomeTracker = this.eventManager.onEvent(EventSocketWelcomedEvent.class, e -> {
            if (set.remove(e.getConnection()))
                latch.countDown();
        });
        for (int i = 0; i < poolShards; i++) {
            TwitchEventSocket socket = TwitchEventSocket.builder()
                .api(api)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .defaultToken(appAccessToken)
                .proxyConfig(proxyConfig)
                .eventManager(this.eventManager)
                .taskExecutor(this.executor)
                .build();
            sockets.add(socket);
            set.add(socket);
        }

        // Wait for all sockets to be connected
        long timeout = socketWelcomeTimeout != null ? socketWelcomeTimeout.toMillis() : 15_000L;
        try {
            if (!latch.await(timeout, TimeUnit.MILLISECONDS)) {
                log.error("Failed to create {} shards", latch.getCount());
            }
        } catch (InterruptedException e) {
            try {
                this.close();
            } catch (Exception ex) {
                log.warn("Failed to clean up conduit pool", ex);
            }
            throw e;
        } finally {
            welcomeTracker.dispose();
        }

        // Clean up sockets that did not connect in time
        sockets.removeIf(socket -> {
            if (socket.getWebsocketId() != null) {
                return false;
            }
            try {
                socket.close();
            } catch (Exception e) {
                log.warn("Failed to destroy socket shard that did not connect in time", e);
            }
            return true;
        });

        // Register shards
        AtomicInteger shardId = new AtomicInteger(shardOffset);
        List<ConduitShard> shards = sockets.stream()
            .map(TwitchEventSocket::getWebsocketId)
            .filter(Objects::nonNull)
            .map(id -> EventSubTransport.builder().method(EventSubTransportMethod.WEBSOCKET).sessionId(id).build())
            .map(t -> ConduitShard.builder().shardId(String.valueOf(shardId.getAndIncrement())).transport(t).build())
            .collect(Collectors.toList());
        try {
            api.updateConduitShards(token, new ShardsInput(this.conduitId, shards)).execute();
        } catch (Exception e) {
            try {
                this.close();
            } catch (Exception ex) {
                log.warn("Failed to clean up conduit pool", ex);
            }
            throw new RuntimeException("Failed to register shards for Conduit with ID: " + this.conduitId, e);
        }
    }

    @Override
    public EventSubSubscription register(@NotNull EventSubSubscription subscription) {
        String token = credential != null ? credential.getAccessToken() : null;
        return api.createEventSubSubscription(token, subscription.withTransport(transport)).execute().getSubscriptions().get(0);
    }

    @Override
    public <C extends EventSubCondition, B> Optional<EventSubSubscription> register(@NotNull SubscriptionType<C, B, ?> type, @NotNull Function<B, C> conditions) {
        EventSubSubscription sub = type.prepareSubscription(conditions, transport);
        try {
            return Optional.ofNullable(this.register(sub));
        } catch (Exception e) {
            log.error("Failed to create EventSub subscription for Conduit with ID {}: {}", conduitId, sub, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean unregister(@NotNull EventSubSubscription subscription) {
        if (subscription.getId() == null && subscription.getType() == null)
            throw new IllegalArgumentException("Subscription to be unregistered is invalid");

        if (subscription.getTransport() != null && !conduitId.equals(subscription.getTransport().getConduitId()))
            throw new IllegalArgumentException("Specified subscription is not registered with this Conduit");

        String token = credential != null ? credential.getAccessToken() : null;

        String id;
        if (subscription.getId() != null) {
            id = subscription.getId();
        } else {
            try {
                // noinspection OptionalGetWithoutIsPresent
                id = api.getEventSubSubscriptions(token, EventSubSubscriptionStatus.ENABLED, subscription.getType(), null, null, null).execute().getSubscriptions().stream().filter(sub -> conduitId.equals(sub.getTransport().getConduitId())).findAny().map(EventSubSubscription::getId).get();
            } catch (Exception e) {
                log.warn("Specified subscription is not actively registered to this Conduit with ID {}: {}", conduitId, subscription, e);
                return false;
            }
        }

        try {
            api.deleteEventSubSubscription(token, id).execute();
            return true;
        } catch (Exception e) {
            log.warn("Failed to delete EventSub subscription from Conduit with ID {}: {}", conduitId, subscription, e);
            return false;
        }
    }

    @Override
    public long getLatency() {
        long sum = 0;
        int count = 0;
        for (TwitchEventSocket socket : sockets) {
            sum += socket.getLatency();
            count++;
        }
        return count > 0 ? sum / count : -1L;
    }

    @Override
    public void close() throws Exception {
        for (TwitchEventSocket socket : sockets) {
            socket.close();
        }

        if (shouldDeleteConduit) {
            String token = credential != null ? credential.getAccessToken() : null;
            api.deleteConduit(token, conduitId).execute();
        }

        if (shouldCloseExecutor) {
            executor.shutdownNow();
        }
    }

    public int getManagedShardCount() {
        return sockets.size();
    }

}
