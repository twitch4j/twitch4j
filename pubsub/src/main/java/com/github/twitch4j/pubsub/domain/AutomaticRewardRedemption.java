package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@Unofficial
public class AutomaticRewardRedemption {

    /**
     * Unique ID for this redemption event.
     */
    private String id;

    /**
     * User that redeemed this reward.
     */
    private ChannelPointsUser user;

    /**
     * ID of the channel in which the reward was redeemed.
     */
    private String channelId;

    /**
     * Timestamp at which a reward was redeemed.
     */
    private Instant redeemedAt;

    /**
     * The reward that was redeemed.
     */
    private AutomaticReward reward;

    /**
     * Additional metadata about the redemption.
     */
    @Nullable
    @Unofficial
    private RedemptionMetadata redemptionMetadata;

}
