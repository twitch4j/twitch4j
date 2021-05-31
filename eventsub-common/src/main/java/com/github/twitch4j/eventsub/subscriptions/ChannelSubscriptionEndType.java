package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelUnsubscribeCondition;
import com.github.twitch4j.eventsub.events.ChannelUnsubscribeEvent;

/**
 * The channel.subscription.end subscription type sends a notification when a subscription to the specified channel expires.
 * <p>
 * Must have channel:read:subscriptions scope.
 */
public class ChannelSubscriptionEndType implements SubscriptionType<ChannelUnsubscribeCondition, ChannelUnsubscribeCondition.ChannelUnsubscribeConditionBuilder<?, ?>, ChannelUnsubscribeEvent> {

    @Override
    public String getName() {
        return "channel.subscription.end";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelUnsubscribeCondition.ChannelUnsubscribeConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelUnsubscribeCondition.builder();
    }

    @Override
    public Class<ChannelUnsubscribeEvent> getEventClass() {
        return ChannelUnsubscribeEvent.class;
    }

}
