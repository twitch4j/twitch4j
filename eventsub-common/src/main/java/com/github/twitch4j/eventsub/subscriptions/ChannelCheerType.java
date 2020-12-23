package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelCheerCondition;
import com.github.twitch4j.eventsub.events.ChannelCheerEvent;

/**
 * A user cheers on the specified channel.
 * <p>
 * Must have bits:read scope.
 */
public class ChannelCheerType implements SubscriptionType<ChannelCheerCondition, ChannelCheerCondition.ChannelCheerConditionBuilder<?, ?>, ChannelCheerEvent> {

    @Override
    public String getName() {
        return "channel.cheer";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelCheerCondition.ChannelCheerConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelCheerCondition.builder();
    }

    @Override
    public Class<ChannelCheerEvent> getEventClass() {
        return ChannelCheerEvent.class;
    }

}
