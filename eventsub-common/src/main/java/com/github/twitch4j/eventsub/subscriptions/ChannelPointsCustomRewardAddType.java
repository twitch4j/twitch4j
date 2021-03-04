package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPointsCustomRewardAddCondition;
import com.github.twitch4j.eventsub.events.CustomRewardAddEvent;

/**
 * A custom channel points reward has been created for the specified channel.
 * <p>
 * Must have channel:read:redemptions or channel:manage:redemptions scope.
 */
public class ChannelPointsCustomRewardAddType implements SubscriptionType<ChannelPointsCustomRewardAddCondition,
    ChannelPointsCustomRewardAddCondition.ChannelPointsCustomRewardAddConditionBuilder<?, ?>, CustomRewardAddEvent> {

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

    @Override
    public Class<CustomRewardAddEvent> getEventClass() {
        return CustomRewardAddEvent.class;
    }

}
