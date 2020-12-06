package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPointsCustomRewardRedemptionUpdateCondition;

/**
 * A redemption of a channel points custom reward has been updated for the specified channel.
 * <p>
 * Must have channel:read:redemptions scope.
 */
public class ChannelPointsCustomRewardRedemptionUpdateType implements SubscriptionType<ChannelPointsCustomRewardRedemptionUpdateCondition,
    ChannelPointsCustomRewardRedemptionUpdateCondition.ChannelPointsCustomRewardRedemptionUpdateConditionBuilder<?, ?>> {

    @Override
    public String getName() {
        return "channel.channel_points_custom_reward_redemption.update";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelPointsCustomRewardRedemptionUpdateCondition.ChannelPointsCustomRewardRedemptionUpdateConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelPointsCustomRewardRedemptionUpdateCondition.builder();
    }

}
