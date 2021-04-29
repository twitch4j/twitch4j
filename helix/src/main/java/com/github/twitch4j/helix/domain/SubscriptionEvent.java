package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class SubscriptionEvent {

    /**
     * ID of the event.
     */
    private String id;

    /**
     * The type of event in the event_data payload.
     */
    private Type eventType;

    /**
     * RFC3339 formatted timestamp for when the event occurred.
     */
    private Instant eventTimestamp;

    /**
     * Event version.
     */
    private String version;

    /**
     * Event payload.
     */
    private Subscription eventData;

    /**
     * The type of event in the event_data payload.
     */
    public enum Type {
        /**
         * When the subscription payment is processed and the user has officially started their subscription.
         */
        @JsonProperty("subscriptions.subscribe")
        SUBSCRIBE,

        /**
         * When a user’s subscription ends.
         */
        @JsonProperty("subscriptions.unsubscribe")
        UNSUBSCRIBE,

        /**
         * When a user who is subscribed to a broadcaster notifies the broadcaster of their subscription in the chat.
         */
        @JsonProperty("subscriptions.notification")
        NOTIFICATION;
    }

}
