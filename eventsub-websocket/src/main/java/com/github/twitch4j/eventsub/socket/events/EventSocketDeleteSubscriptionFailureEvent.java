package com.github.twitch4j.eventsub.socket.events;

import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.socket.TwitchEventSocket;
import lombok.Value;

/**
 * Called when an EventSocket fails to delete a subscription via the Helix API.
 */
@Value
public class EventSocketDeleteSubscriptionFailureEvent {

    /**
     * The subscription that was attempted to be deleted.
     */
    EventSubSubscription subscription;

    /**
     * The EventSocket attempting to delete the subscription.
     */
    TwitchEventSocket connection;

    /**
     * The exception associated with the deletion failure.
     */
    Throwable error;

}
