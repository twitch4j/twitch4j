package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPointsCustomRewardAddCondition;

/**
 * A custom channel points reward has been created for the specified channel.
 * <p>
 * Must have channel:read:redemptions scope.
 */
public class ChannelPointsCustomRewardAddType implements SubscriptionType<ChannelPointsCustomRewardAddCondition,
    ChannelPointsCustomRewardAddCondition.ChannelPointsCustomRewardAddConditionBuilder<?, ?>> {

    @Override
    public String getName() {
        return "channel.channel_points_custom_reward.add";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelPointsCustomRewardAddCondition.ChannelPointsCustomRewardAddConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelPointsCustomRewardAddCondition.builder();
    }

}
