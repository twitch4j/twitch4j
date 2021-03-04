package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.RedemptionStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class CustomRewardRedemption {

    /**
     * The id of the broadcaster that the reward belongs to.
     */
    private String broadcasterId;

    /**
     * Broadcaster’s user login name.
     */
    private String broadcasterLogin;

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
     * The login of the user who redeemed the reward.
     */
    private String userLogin;

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
