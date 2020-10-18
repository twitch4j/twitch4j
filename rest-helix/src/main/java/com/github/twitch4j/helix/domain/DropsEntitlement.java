package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * An entitlement is the link between a User and a Benefit.
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DropsEntitlement {

    /**
     * Unique Identifier of the entitlement
     */
    private String id;

    /**
     * Identifier of the Benefit
     */
    private String benefitId;

    /**
     * UTC timestamp in ISO format when this entitlement was granted on Twitch.
     */
    private Instant timestamp;

    /**
     * Twitch User ID of the user who was granted the entitlement.
     */
    private String userId;

    /**
     * Twitch Game ID of the game that was being played when this benefit was entitled.
     */
    private String gameId;

}
