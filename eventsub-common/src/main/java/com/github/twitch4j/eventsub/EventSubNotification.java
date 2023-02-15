package com.github.twitch4j.eventsub;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.eventsub.events.EventSubEvent;
import com.github.twitch4j.eventsub.util.NotificationDeserializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonDeserialize(using = NotificationDeserializer.class)
public class EventSubNotification {

    /**
     * Metadata about the subscription.
     */
    private EventSubSubscription subscription;

    /**
     * The event information. The fields inside this object differ by subscription type.
     */
    private EventSubEvent event;

    /**
     * The value of challenge from the callback verification request must be returned to complete the verification process.
     * <p>
     * Note: websocket subscriptions do not involve a challenge handshake.
     */
    @Nullable
    private String challenge;

}
