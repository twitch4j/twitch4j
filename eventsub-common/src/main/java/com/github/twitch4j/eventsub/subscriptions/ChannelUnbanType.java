package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelUnbanCondition;

/**
 * A viewer is unbanned from the specified channel.
 * <p>
 * Must have channel:moderate scope.
 */
public class ChannelUnbanType implements SubscriptionType<ChannelUnbanCondition, ChannelUnbanCondition.ChannelUnbanConditionBuilder<?, ?>> {

    @Override
    public String getName() {
        return "channel.unban";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelUnbanCondition.ChannelUnbanConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelUnbanCondition.builder();
    }

}
