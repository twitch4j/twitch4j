package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPointsCustomRewardUpdateCondition;

/**
 * A custom channel points reward has been updated for the specified channel.
 * <p>
 * Must have channel:read:redemptions scope.
 */
public class ChannelPointsCustomRewardUpdateType implements SubscriptionType<ChannelPointsCustomRewardUpdateCondition,
    ChannelPointsCustomRewardUpdateCondition.ChannelPointsCustomRewardUpdateConditionBuilder<?, ?>> {

    @Override
    public String getName() {
        return "channel.channel_points_custom_reward.update";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelPointsCustomRewardUpdateCondition.ChannelPointsCustomRewardUpdateConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelPointsCustomRewardUpdateCondition.builder();
    }

}
