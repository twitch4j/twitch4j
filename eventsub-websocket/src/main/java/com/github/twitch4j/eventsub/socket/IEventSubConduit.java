package com.github.twitch4j.eventsub.socket;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.condition.EventSubCondition;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

public interface IEventSubConduit extends AutoCloseable {

    /**
     * @return the ID associated with this Conduit
     */
    String getConduitId();

    /**
     * @return the event manager for eventsub notifications
     */
    IEventManager getEventManager();

    /**
     * Creates an eventsub subscription for this conduit.
     *
     * @param subscription the eventsub subscription to be registered
     * @return the created subscription
     * @apiNote An exception is thrown if the subscription could not be created.
     * Prefer {@link #register(SubscriptionType, Function)} if your code does not inspect the specific exception.
     */
    EventSubSubscription register(@NotNull EventSubSubscription subscription);

    /**
     * Creates an eventsub subscription for this conduit.
     *
     * @param type       the type of EventSub subscription to be created; see {@link com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes}
     * @param conditions the conditions associated with the eventsub subscription
     * @param <C>        the condition type associated with the subscription
     * @param <B>        the builder type associated with the condition
     * @return the created subscription, or empty if the subscription could not be registered.
     * @apiNote No exception is thrown if helix rejects the registration request
     */
    <C extends EventSubCondition, B> Optional<EventSubSubscription> register(@NotNull SubscriptionType<C, B, ?> type,
                                                                             @NotNull Function<B, C> conditions);

    /**
     * Deletes an eventsub subscription from helix (and this conduit).
     *
     * @param subscription the eventsub subscription to be destroyed
     * @return whether the specified subscription was successfully deleted
     */
    boolean unregister(@NotNull EventSubSubscription subscription);

    /**
     * @return the average latency for the socket shards in milliseconds, or -1 if unknown
     * @implNote This method only considers the subset of shards that are managed by this object.
     */
    long getLatency();

}
