package com.github.twitch4j.common.pool;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.common.config.ProxyConfig;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Supplier;

/**
 * Adds common configuration options for subscription-based connection pools built around instances of T4J modules.
 * <p>
 * {@inheritDoc}
 */
@SuperBuilder
public abstract class TwitchModuleConnectionPool<C, X, Y, Z> extends SubscriptionConnectionPool<C, X, Y, Z> {

    /**
     * The default {@link EventManager} for this connection pool, if specified.
     */
    @Getter
    @Builder.Default
    private final EventManager eventManager = createEventManager();

    /**
     * The {@link EventManager} to be used by connections in this pool.
     * <p>
     * If the supplier itself is null, the connection will use the pool's aggregate {@link EventManager}.
     * If the supplier yields null, the connection will use its own {@link EventManager}.
     */
    @Builder.Default
    private final Supplier<EventManager> connectionEventManager = null;

    /**
     * The {@link ScheduledThreadPoolExecutor} to be used by connections in this pool, if specified.
     */
    @NonNull
    @Builder.Default
    protected final Supplier<ScheduledThreadPoolExecutor> executor = () -> null;

    /**
     * The {@link ProxyConfig} to be used by connections in this pool, if specified.
     */
    @NonNull
    @Builder.Default
    protected final Supplier<ProxyConfig> proxyConfig = () -> null;

    /**
     * @return a {@link EventManager} to be used in the construction of a new connection
     */
    protected EventManager getConnectionEventManager() {
        // noinspection ConstantConditions (does not seem to recognize that SuperBuilder may set the field)
        if (connectionEventManager != null)
            return this.connectionEventManager.get();

        return getDefaultConnectionEventManager();
    }

    /**
     * @return the {@link EventManager} to use if none was specified by the user for the connection-level
     */
    protected EventManager getDefaultConnectionEventManager() {
        return this.getEventManager();
    }

    protected static EventManager createEventManager() {
        EventManager em = new EventManager();
        em.autoDiscovery();
        em.setDefaultEventHandler(SimpleEventHandler.class); // Unfortunately hard-coded due to the static nature of @Builder.Default; the user can override later
        return em;
    }

}
