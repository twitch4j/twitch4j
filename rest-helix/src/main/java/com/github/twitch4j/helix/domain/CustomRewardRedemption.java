package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.twitch4j.eventsub.domain.RedemptionStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomRewardRedemption {

    /**
     * The id of the broadcaster that the reward belongs to.
     */
    private String broadcasterId;

    /**
     * The display name of the broadcaster that the reward belongs to.
     */
    private String broadcasterName;

    /**
     * The ID of the redemption.
     */
    @JsonProperty("id")
    private String redemptionId; // renamed to avoid confusion of reward versus redemption id

    /**
     * The ID of the user that redeemed the reward.
     */
    private String userId;

    /**
     * The display name of the user that redeemed the reward.
     */
    private String userName;

    /**
     * Basic information about the Custom Reward that was redeemed at the time it was redeemed.
     */
    private CustomReward reward;

    /**
     * The user input provided.
     * Empty string if not provided.
     */
    private String userInput;

    /**
     * The redemption status.
     */
    private RedemptionStatus status;

    /**
     * RFC3339 timestamp of when the reward was redeemed.
     */
    private Instant redeemedAt;

}
