package com.github.twitch4j.eventsub.socket.conduit;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.eventsub.socket.IEventSubConduit;
import com.github.twitch4j.eventsub.socket.conduit.exceptions.ShardTimeoutException;
import com.github.twitch4j.helix.TwitchHelix;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Consumer;

@Data
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConduitSpec {

    /**
     * The ID of an already created Conduit (optional);
     * otherwise {@link TwitchConduitSocketPool} will create a new Conduit.
     */
    private @Nullable String conduitId;

    /**
     * The number of websockets that should be created by the {@link TwitchConduitSocketPool} instance.
     * <p>
     * Must be positive.
     */
    private int poolShards;

    /**
     * The total number of shards associated with the Conduit ID (optional).
     * <p>
     * If the Conduit is already created, specifying this value avoids a {@link TwitchHelix#getConduits(String)} call.
     * <p>
     * If the Conduit is not created yet, this field can be used to create a larger Conduit than {@link #poolShards()}
     * (if you plan to create another {@link TwitchConduitSocketPool} instance on another server).
     */
    private @Nullable Integer totalShardCount;

    /**
     * An integer offset for the Shard IDs to be registered with the Conduit.
     * <p>
     * This field should only be set if the Conduit is already created with some shards already registered.
     */
    private int shardOffset;

    /**
     * Your application's client ID.
     * <p>
     * Not required if {@link #appAccessToken()} is specified or {@link #helix()} already has client credentials.
     */
    private @Nullable String clientId;

    /**
     * Your application's client secret.
     * <p>
     * Not required if {@link #appAccessToken()} is specified or {@link #helix()} already has client credentials.
     */
    private @Nullable String clientSecret;

    /**
     * An optional {@link TwitchHelix} instance (if one is already created).
     */
    private @Nullable TwitchHelix helix;

    /**
     * The app access token for helix requests.
     * <p>
     * Not required if client id and secret are specified OR the specified helix instance already has client credentials.
     */
    private @Nullable OAuth2Credential appAccessToken;

    /**
     * An optional {@link ScheduledThreadPoolExecutor}.
     */
    private @Nullable ScheduledThreadPoolExecutor executor;

    /**
     * An optional {@link EventManager} (if one is already created).
     * <p>
     * Prefer using {@link IEventSubConduit#getEventManager()} (instead of creating an {@link EventManager}).
     */
    private @Nullable EventManager eventManager;

    /**
     * Optional: the proxy to use for helix requests and the websocket connections.
     */
    private @Nullable ProxyConfig proxyConfig;

    /**
     * The amount of time to wait for the websocket shards to be welcomed by Twitch.
     * <p>
     * If a shard does not connect within this timeout, {@link ShardTimeoutException} is thrown by {@link TwitchConduitSocketPool}.
     */
    private @Nullable Duration socketWelcomeTimeout;

    public void validate() {
        if (poolShards <= 0)
            throw new IllegalArgumentException("Pool must have a positive number of websocket shards");

        if (shardOffset < 0)
            throw new IllegalArgumentException("Invalid shard offset");

        if (conduitId == null && totalShardCount != null && totalShardCount < poolShards)
            throw new IllegalArgumentException("Cannot create more sockets than total shards");

        if (appAccessToken == null && (clientId == null || clientSecret == null) && helix == null && conduitId == null)
            throw new IllegalArgumentException("Conduit pool is missing authorization");

        if (appAccessToken != null && appAccessToken.getUserId() != null && !appAccessToken.getUserId().isEmpty())
            throw new IllegalArgumentException("Access token must be associated with an app rather than a user");

        if (socketWelcomeTimeout != null && (socketWelcomeTimeout.isNegative() || socketWelcomeTimeout.isZero()))
            throw new IllegalArgumentException("Socket welcome timeout must be positive");
    }

    @NotNull
    public static ConduitSpec process(@NotNull Consumer<ConduitSpec> spec) {
        ConduitSpec config = new ConduitSpec();
        spec.accept(config);
        config.validate();
        return config;
    }

}
