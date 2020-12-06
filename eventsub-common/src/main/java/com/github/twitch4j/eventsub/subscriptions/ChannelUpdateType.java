package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelUpdateCondition;

/**
 * A broadcaster updates their channel properties e.g., category, title, mature flag, broadcast, or language.
 * <p>
 * No authorization required.
 */
public class ChannelUpdateType implements SubscriptionType<ChannelUpdateCondition, ChannelUpdateCondition.ChannelUpdateConditionBuilder<?, ?>> {

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

}
