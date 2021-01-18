package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.RedemptionStatus;
import com.github.twitch4j.eventsub.domain.Reward;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class ChannelPointsCustomRewardRedemptionEvent extends EventSubUserChannelEvent {

    /**
     * The redemption identifier.
     */
    private String id;

    /**
     * The user input provided. Empty string if not provided.
     */
    private String userInput;

    /**
     * Defaults to unfulfilled. Possible values are unknown, unfulfilled, fulfilled, and canceled.
     */
    private RedemptionStatus status;

    /**
     * Basic information about the reward that was redeemed, at the time it was redeemed.
     */
    private Reward reward;

    /**
     * RFC3339 timestamp of when the reward was redeemed.
     */
    private Instant redeemedAt;

}
