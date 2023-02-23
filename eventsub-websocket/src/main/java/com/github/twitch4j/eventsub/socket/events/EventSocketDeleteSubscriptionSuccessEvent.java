package com.github.twitch4j.eventsub.socket.events;

import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.socket.TwitchEventSocket;
import lombok.Value;

/**
 * Called when an EventSocket successfully deletes a subscription via the Helix API.
 */
@Value
public class EventSocketDeleteSubscriptionSuccessEvent {

    /**
     * The eventsub subscription that was successfully deleted.
     */
    EventSubSubscription subscription;

    /**
     * The EventSocket connection associated with the subscription.
     */
    TwitchEventSocket connection;

}
