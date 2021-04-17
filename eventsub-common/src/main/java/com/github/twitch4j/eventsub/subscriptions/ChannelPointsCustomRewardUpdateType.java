package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPointsCustomRewardUpdateCondition;
import com.github.twitch4j.eventsub.events.CustomRewardUpdateEvent;

/**
 * A custom channel points reward has been updated for the specified channel.
 * <p>
 * Must have channel:read:redemptions or channel:manage:redemptions scope.
 */
public class ChannelPointsCustomRewardUpdateType implements SubscriptionType<ChannelPointsCustomRewardUpdateCondition,
    ChannelPointsCustomRewardUpdateCondition.ChannelPointsCustomRewardUpdateConditionBuilder<?, ?>, CustomRewardUpdateEvent> {

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

    @Override
    public Class<CustomRewardUpdateEvent> getEventClass() {
        return CustomRewardUpdateEvent.class;
    }

}
