package com.github.twitch4j.eventsub.socket.events;

import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.socket.TwitchEventSocket;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * Called when an EventSocket fails to register subscription with the Helix API.
 */
@Value
public class EventSocketSubscriptionFailureEvent {

    /**
     * The subscription that was attempted to be created.
     */
    EventSubSubscription subscription;

    /**
     * The EventSocket attempting to create the subscription.
     */
    TwitchEventSocket connection;

    /**
     * The exception associated with the subscription failure.
     */
    Throwable error;

    /**
     * Whether the library will automatically try to create the subscription again (at the next reconnect).
     */
    @Accessors(fluent = true)
    boolean willRetry;

}
