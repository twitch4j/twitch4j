package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelUpdateCondition;
import com.github.twitch4j.eventsub.events.ChannelUpdateEvent;

/**
 * A broadcaster updates their channel properties e.g., category, title, mature flag, broadcast, or language.
 * <p>
 * No authorization required.
 *
 * @deprecated in favor of {@link ChannelUpdateV2Type}
 */
@Deprecated
public class ChannelUpdateType implements SubscriptionType<ChannelUpdateCondition, ChannelUpdateCondition.ChannelUpdateConditionBuilder<?, ?>, ChannelUpdateEvent> {

    @Override
    public String getName() {
        return "channel.update";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelUpdateCondition.ChannelUpdateConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelUpdateCondition.builder();
    }

    @Override
    public Class<ChannelUpdateEvent> getEventClass() {
        return ChannelUpdateEvent.class;
    }

}
