package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelSubscribeCondition;
import com.github.twitch4j.eventsub.events.ChannelSubscribeEvent;

/**
 * A notification when a specified channel receives a subscriber.
 * This does not include resubscribes.
 * <p>
 * Must have channel:read:subscriptions scope.
 */
public class ChannelSubscribeType implements SubscriptionType<ChannelSubscribeCondition, ChannelSubscribeCondition.ChannelSubscribeConditionBuilder<?, ?>, ChannelSubscribeEvent> {

    @Override
    public String getName() {
        return "channel.subscribe";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelSubscribeCondition.ChannelSubscribeConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelSubscribeCondition.builder();
    }

    @Override
    public Class<ChannelSubscribeEvent> getEventClass() {
        return ChannelSubscribeEvent.class;
    }

}
