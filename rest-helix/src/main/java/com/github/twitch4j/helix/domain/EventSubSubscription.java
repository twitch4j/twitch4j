package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.twitch4j.helix.eventsub.domain.EventSubSubscriptionStatus;
import com.github.twitch4j.helix.eventsub.domain.EventSubTransport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventSubSubscription {

    /**
     * ID of the subscription.
     */
    private String id;

    /**
     * Status of the subscription.
     */
    private EventSubSubscriptionStatus status;

    /**
     * The category of the subscription.
     */
    private String type;

    /**
     * The version of the subscription.
     */
    private String version;

    /**
     * Object specifying custom parameters for the subscription.
     */
    private Map<String, Object> condition;

    /**
     * Timestamp indicating when the subscription was created.
     */
    private Instant createdAt;

    /**
     * Object indicating the notification delivery specific information
     */
    private EventSubTransport transport;

}
