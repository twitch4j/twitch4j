package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.condition.ChannelSubscriptionGiftCondition;
import com.github.twitch4j.eventsub.events.ChannelSubscriptionGiftEvent;

/**
 * The channel.subscription.gift subscription type sends a notification when a user gives one or more gifted subscriptions in a channel.
 * <p>
 * Must have channel:read:subscriptions scope.
 * <p>
 * Unless otherwise noted, EventSub subscriptions that were released as a public beta will be available for 30 days after their v1 version is released.
 * Subscriptions should be updated to v1 during this timeframe. Any active beta subscriptions beyond 30 days will be automatically deleted.
 */
@Unofficial
public class BetaChannelSubscriptionGiftType implements SubscriptionType<ChannelSubscriptionGiftCondition, ChannelSubscriptionGiftCondition.ChannelSubscriptionGiftConditionBuilder<?, ?>, ChannelSubscriptionGiftEvent> {

    @Override
    public String getName() {
        return "channel.subscription.gift";
    }

    @Override
    public String getVersion() {
        return "beta";
    }

    @Override
    public ChannelSubscriptionGiftCondition.ChannelSubscriptionGiftConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelSubscriptionGiftCondition.builder();
    }

    @Override
    public Class<ChannelSubscriptionGiftEvent> getEventClass() {
        return ChannelSubscriptionGiftEvent.class;
    }

}
