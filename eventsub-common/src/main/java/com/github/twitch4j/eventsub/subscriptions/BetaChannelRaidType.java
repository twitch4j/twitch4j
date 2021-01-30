package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.condition.ChannelRaidCondition;
import com.github.twitch4j.eventsub.events.ChannelRaidEvent;

/**
 * A broadcaster raids another broadcaster’s channel.
 * <p>
 * No authorization required.
 * <p>
 * This {@link SubscriptionType} is marked as {@link Unofficial} due to Twitch indicating that it is not intended for use in production environments.
 */
@Unofficial
public class BetaChannelRaidType implements SubscriptionType<ChannelRaidCondition, ChannelRaidCondition.ChannelRaidConditionBuilder<?, ?>, ChannelRaidEvent> {

    @Override
    public String getName() {
        return "channel.raid";
    }

    @Override
    public String getVersion() {
        return "beta";
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
