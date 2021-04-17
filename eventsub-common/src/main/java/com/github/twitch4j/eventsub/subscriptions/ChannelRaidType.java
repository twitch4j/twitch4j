package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelRaidCondition;
import com.github.twitch4j.eventsub.events.ChannelRaidEvent;

/**
 * A broadcaster raids another broadcaster’s channel.
 * <p>
 * No authorization required.
 */
public class ChannelRaidType implements SubscriptionType<ChannelRaidCondition, ChannelRaidCondition.ChannelRaidConditionBuilder<?, ?>, ChannelRaidEvent> {

    @Override
    public String getName() {
        return "channel.raid";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelRaidCondition.ChannelRaidConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelRaidCondition.builder();
    }

    @Override
    public Class<ChannelRaidEvent> getEventClass() {
        return ChannelRaidEvent.class;
    }

}
