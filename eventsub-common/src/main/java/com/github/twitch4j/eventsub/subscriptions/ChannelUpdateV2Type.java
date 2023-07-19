package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelUpdateCondition;
import com.github.twitch4j.eventsub.events.ChannelUpdateV2Event;

/**
 * A broadcaster updates their channel properties e.g., category, title, content classification labels, or broadcast language.
 * <p>
 * No authorization required.
 */
public class ChannelUpdateV2Type implements SubscriptionType<ChannelUpdateCondition, ChannelUpdateCondition.ChannelUpdateConditionBuilder<?, ?>, ChannelUpdateV2Event> {

    @Override
    public String getName() {
        return "channel.update";
    }

    @Override
    public String getVersion() {
        return "2";
    }

    @Override
    public ChannelUpdateCondition.ChannelUpdateConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelUpdateCondition.builder();
    }

    @Override
    public Class<ChannelUpdateV2Event> getEventClass() {
        return ChannelUpdateV2Event.class;
    }

}
