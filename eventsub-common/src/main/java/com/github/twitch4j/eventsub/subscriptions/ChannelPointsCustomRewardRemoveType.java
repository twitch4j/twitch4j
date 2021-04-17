package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPointsCustomRewardRemoveCondition;
import com.github.twitch4j.eventsub.events.CustomRewardRemoveEvent;

/**
 * A custom channel points reward has been removed from the specified channel.
 * <p>
 * Must have channel:read:redemptions or channel:manage:redemptions scope.
 */
public class ChannelPointsCustomRewardRemoveType implements SubscriptionType<ChannelPointsCustomRewardRemoveCondition,
    ChannelPointsCustomRewardRemoveCondition.ChannelPointsCustomRewardRemoveConditionBuilder<?, ?>, CustomRewardRemoveEvent> {

    @Override
    public String getName() {
        return "channel.channel_points_custom_reward.remove";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelPointsCustomRewardRemoveCondition.ChannelPointsCustomRewardRemoveConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelPointsCustomRewardRemoveCondition.builder();
    }

    @Override
    public Class<CustomRewardRemoveEvent> getEventClass() {
        return CustomRewardRemoveEvent.class;
    }

}
