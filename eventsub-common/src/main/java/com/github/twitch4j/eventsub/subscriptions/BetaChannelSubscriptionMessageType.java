package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.condition.ChannelSubscriptionMessageCondition;
import com.github.twitch4j.eventsub.events.ChannelSubscriptionMessageEvent;

/**
 * The channel.subscription.message subscription type sends a notification when a user sends a re-subscription chat message in a specific channel.
 * <p>
 * Must have channel:read:subscriptions scope.
 * <p>
 * Unless otherwise noted, EventSub subscriptions that were released as a public beta will be available for 30 days after their v1 version is released.
 * Subscriptions should be updated to v1 during this timeframe. Any active beta subscriptions beyond 30 days will be automatically deleted.
 */
@Unofficial
public class BetaChannelSubscriptionMessageType implements SubscriptionType<ChannelSubscriptionMessageCondition, ChannelSubscriptionMessageCondition.ChannelSubscriptionMessageConditionBuilder<?, ?>, ChannelSubscriptionMessageEvent> {

    @Override
    public String getName() {
        return "channel.subscription.message";
    }

    @Override
    public String getVersion() {
        return "beta";
    }

    @Override
    public ChannelSubscriptionMessageCondition.ChannelSubscriptionMessageConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelSubscriptionMessageCondition.builder();
    }

    @Override
    public Class<ChannelSubscriptionMessageEvent> getEventClass() {
        return ChannelSubscriptionMessageEvent.class;
    }

}
