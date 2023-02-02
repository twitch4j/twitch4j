package com.github.twitch4j.eventsub.socket.events;

import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.socket.TwitchEventSocket;
import lombok.Value;

/**
 * Called when an EventSocket successfully registers a subscription with the Helix API.
 */
@Value
public class EventSocketSubscriptionSuccessEvent {

    /**
     * The eventsub subscription that was successfully created.
     */
    EventSubSubscription subscription;

    /**
     * The EventSocket connection associated with the subscription.
     */
    TwitchEventSocket connection;

}
