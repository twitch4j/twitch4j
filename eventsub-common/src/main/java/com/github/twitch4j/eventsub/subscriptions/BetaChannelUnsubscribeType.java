package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelUnsubscribeCondition;
import com.github.twitch4j.eventsub.events.ChannelUnsubscribeEvent;

/**
 * The channel.unsubscribe subscription type sends a notification when a subscription to the specified channel expires.
 * <p>
 * Must have channel:read:subscriptions scope.
 * <p>
 * Unless otherwise noted, EventSub subscriptions that were released as a public beta will be available for 30 days after their v1 version is released. Subscriptions should be updated to v1 during this timeframe.
 * Any active beta subscriptions beyond 30 days will be automatically deleted.
 */
public class BetaChannelUnsubscribeType implements SubscriptionType<ChannelUnsubscribeCondition, ChannelUnsubscribeCondition.ChannelUnsubscribeConditionBuilder<?, ?>, ChannelUnsubscribeEvent> {

    @Override
    public String getName() {
        return "channel.unsubscribe";
    }

    @Override
    public String getVersion() {
        return "beta";
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
