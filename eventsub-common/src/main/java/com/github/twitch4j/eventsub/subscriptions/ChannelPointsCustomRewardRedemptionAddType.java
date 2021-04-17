package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPointsCustomRewardRedemptionAddCondition;
import com.github.twitch4j.eventsub.events.CustomRewardRedemptionAddEvent;

/**
 * A viewer has redeemed a custom channel points reward on the specified channel.
 * <p>
 * Must have channel:read:redemptions or channel:manage:redemptions scope.
 */
public class ChannelPointsCustomRewardRedemptionAddType implements SubscriptionType<ChannelPointsCustomRewardRedemptionAddCondition,
    ChannelPointsCustomRewardRedemptionAddCondition.ChannelPointsCustomRewardRedemptionAddConditionBuilder<?, ?>, CustomRewardRedemptionAddEvent> {

    @Override
    public String getName() {
        return "channel.channel_points_custom_reward_redemption.add";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelPointsCustomRewardRedemptionAddCondition.ChannelPointsCustomRewardRedemptionAddConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelPointsCustomRewardRedemptionAddCondition.builder();
    }

    @Override
    public Class<CustomRewardRedemptionAddEvent> getEventClass() {
        return CustomRewardRedemptionAddEvent.class;
    }

}
