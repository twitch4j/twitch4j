package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelFollowCondition;
import com.github.twitch4j.eventsub.events.ChannelFollowEvent;

/**
 * A specified channel receives a follow.
 * <p>
 * No authorization required.
 */
public class ChannelFollowType implements SubscriptionType<ChannelFollowCondition, ChannelFollowCondition.ChannelFollowConditionBuilder<?, ?>, ChannelFollowEvent> {

    @Override
    public String getName() {
        return "channel.follow";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelFollowCondition.ChannelFollowConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelFollowCondition.builder();
    }

    @Override
    public Class<ChannelFollowEvent> getEventClass() {
        return ChannelFollowEvent.class;
    }

}
