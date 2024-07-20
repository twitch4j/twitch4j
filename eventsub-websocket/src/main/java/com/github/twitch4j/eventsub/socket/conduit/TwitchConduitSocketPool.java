package com.github.twitch4j.eventsub.socket.conduit;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.common.util.EventManagerUtils;
import com.github.twitch4j.common.util.ThreadUtils;
import com.github.twitch4j.eventsub.Conduit;
import com.github.twitch4j.eventsub.ConduitShard;
import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.EventSubSubscriptionStatus;
import com.github.twitch4j.eventsub.EventSubTransport;
import com.github.twitch4j.eventsub.EventSubTransportMethod;
import com.github.twitch4j.eventsub.condition.EventSubCondition;
import com.github.twitch4j.eventsub.socket.IEventSubConduit;
import com.github.twitch4j.eventsub.socket.TwitchEventSocket;
import com.github.twitch4j.eventsub.socket.conduit.exceptions.ConduitNotFoundException;
import com.github.twitch4j.eventsub.socket.conduit.exceptions.ConduitResizeException;
import com.github.twitch4j.eventsub.socket.conduit.exceptions.CreateConduitException;
import com.github.twitch4j.eventsub.socket.conduit.exceptions.ShardRegistrationException;
import com.github.twitch4j.eventsub.socket.conduit.exceptions.ShardTimeoutException;
import com.github.twitch4j.eventsub.socket.events.ConduitShardReassociationFailureEvent;
import com.github.twitch4j.eventsub.socket.events.EventSocketWelcomedEvent;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.helix.domain.ShardsInput;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Facilitates creating EventSub WebSocket shards for a new or existing Conduit.
 * <p>
 * Sample usage:
 * <pre>
 * {@code
 * IEventSubConduit conduit = TwitchConduitSocketPool.create(spec -> {
 *     spec.poolShards(4); // customizable pool size
 *     spec.clientId("your-client-id");
 *     spec.clientSecret("your-client-secret");
 * });
 * conduit.register(SubscriptionTypes.STREAM_ONLINE, b -> b.broadcasterUserId("71092938").build());
 * conduit.getEventManager().onEvent(StreamOnlineEvent.class, System.out::println);
 * }
 * </pre>
 *
 * @see <a href="https://discuss.dev.twitch.com/t/available-today-twitch-chat-on-eventsub-an-api-for-sending-chat-and-the-conduit-transport-method-for-eventsub/54596#introducing-conduits-an-eventsub-transport-method-for-scale-4">Conduits Announcement</a>
 * @see <a href="https://dev.twitch.tv/docs/eventsub/handling-conduit-events/">Official Documentation</a>
 */
@Slf4j
@SuppressWarnings("unused")
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

    TwitchConduitSocketPool(@NotNull ConduitSpec spec) throws CreateConduitException, ConduitNotFoundException, ConduitResizeException, ShardTimeoutException, ShardRegistrationException {
        this.credential = spec.appAccessToken();
        this.eventManager = EventManagerUtils.validateOrInitializeEventManager(spec.eventManager(), SimpleEventHandler.class);
        this.shardOffset = spec.shardOffset();

        if (spec.executor() == null) {
            String threadPrefix = "twitch4j-conduit-pool-" + CryptoUtils.generateNonce(4) + "-eventsub-ws-";
            this.executor = ThreadUtils.getDefaultScheduledThreadPoolExecutor(threadPrefix, Runtime.getRuntime().availableProcessors());
            this.shouldCloseExecutor = true;
        } else {
            this.executor = spec.executor();
            this.shouldCloseExecutor = false;
        }

        if (spec.helix() == null) {
            this.api = TwitchHelixBuilder.builder()
                .withClientId(spec.clientId())
                .withClientSecret(spec.clientSecret())
                .withDefaultAuthToken(credential)
                .withProxyConfig(spec.proxyConfig())
                .withScheduledThreadPoolExecutor(this.executor)
                .build();
        } else {
            this.api = spec.helix();
        }

        // Create conduit if it does not exist
        final int poolShards = spec.poolShards();
        String token = credential != null ? credential.getAccessToken() : null;
        if (spec.conduitId() == null) {
            int totalShards = spec.totalShardCount() != null ? spec.totalShardCount() : poolShards;
            this.shouldDeleteConduit = poolShards >= totalShards;
            try {
                this.conduitId = api.createConduit(token, totalShards).execute().getConduits().get(0).getId();
            } catch (Exception e) {
                if (shouldCloseExecutor) {
                    this.executor.shutdownNow();
                }
                throw new CreateConduitException(e);
            }
        } else {
            this.conduitId = spec.conduitId();
            this.shouldDeleteConduit = false;

            int totalShards;
            if (spec.totalShardCount() != null) {
                totalShards = spec.totalShardCount();
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
                    throw new ConduitNotFoundException(conduitId, e);
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
                    throw new ConduitResizeException(conduitId, e);
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
                .clientId(spec.clientId())
                .clientSecret(spec.clientSecret())
                .defaultToken(spec.appAccessToken())
                .proxyConfig(spec.proxyConfig())
                .eventManager(this.eventManager)
                .taskExecutor(this.executor)
                .build();
            sockets.add(socket);
            set.add(socket);
        }

        // Wait for all sockets to be connected
        long timeout = spec.socketWelcomeTimeout() != null ? spec.socketWelcomeTimeout().toMillis() : 15_000L;
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
            throw new ShardTimeoutException(timeout);
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
            throw new ShardRegistrationException(this.conduitId, e);
        }

        // Update shards as the underlying websockets reconnect after transient network issues
        eventManager.onEvent(EventSocketWelcomedEvent.class, e -> {
            if (!e.isSessionChanged()) return;
            int shardIndex = sockets.indexOf(e.getConnection());
            if (shardIndex < 0) return;
            String id = String.valueOf(shardIndex + shardOffset);
            EventSubTransport shardTransport = EventSubTransport.builder()
                .method(EventSubTransportMethod.WEBSOCKET)
                .sessionId(e.getSessionId())
                .build();
            ConduitShard updatedShard = ConduitShard.builder()
                .shardId(id)
                .transport(shardTransport)
                .build();
            executor.execute(() -> {
                try {
                    api.updateConduitShards(token, new ShardsInput(this.conduitId, Collections.singletonList(updatedShard))).execute();
                } catch (Exception ex) {
                    log.warn("Failed to re-associate websocket (ID: {}) with conduit (ID: {}) after reconnect", id, this.conduitId, ex);
                    eventManager.publish(new ConduitShardReassociationFailureEvent(e.getConnection(), this, id, ex));
                }
            });
        });
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

    /**
     * Creates a managed websocket pool to serve as shards for an EventSub Conduit.
     * <p>
     * This pool can be used to create a completely new conduit, or augment an existing conduit.
     * <p>
     * Note: Twitch limits each Client ID to five (5) enabled conduits, with up to 20,000 shards per conduit.
     * Please do <i>not</i> try to create 20,000 websockets from a <b>single</b> server.
     *
     * @param spec the specification by which to create a websocket pool for an EventSub Conduit
     * @return a {@link TwitchConduitSocketPool} instance
     * @throws NullPointerException       if the passed spec is {@code null}
     * @throws IllegalArgumentException   if the specified conduit configuration is invalid
     * @throws CreateConduitException     if {@link TwitchHelix#createConduit(String, int)} fails
     * @throws ConduitNotFoundException   if a specific Conduit ID was specified, but not found within {@link TwitchHelix#getConduits(String)} when checking the shard count
     * @throws ConduitResizeException     if {@link TwitchHelix#updateConduit(String, String, int)} fails while resizing the Conduit to accommodate the number of shards in the spec
     * @throws ShardTimeoutException      if any of the underlying sockets for this pool do not receive session_welcome from Twitch before the timeout in the spec (default: 15 seconds)
     * @throws ShardRegistrationException if {@link TwitchHelix#updateConduitShards(String, ShardsInput)} fails while registering the created sockets with the Conduit ID
     * @see <a href="https://dev.twitch.tv/docs/eventsub/handling-conduit-events/">Official Conduits Documentation</a>
     */
    @NotNull
    public static TwitchConduitSocketPool create(@NotNull Consumer<ConduitSpec> spec) throws CreateConduitException, ConduitNotFoundException, ConduitResizeException, ShardTimeoutException, ShardRegistrationException {
        return new TwitchConduitSocketPool(ConduitSpec.process(spec));
    }

}
