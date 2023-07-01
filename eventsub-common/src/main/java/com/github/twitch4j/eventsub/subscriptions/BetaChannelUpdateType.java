package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelUpdateCondition;
import com.github.twitch4j.eventsub.events.BetaChannelUpdateEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * A broadcaster updates their channel properties e.g., category, title, content classification labels, or broadcast language.
 * <p>
 * No authorization required.
 * <p>
 * This subscription type is in open beta since 2023-06-29.
 */
@ApiStatus.Experimental
public class BetaChannelUpdateType implements SubscriptionType<ChannelUpdateCondition, ChannelUpdateCondition.ChannelUpdateConditionBuilder<?, ?>, BetaChannelUpdateEvent> {

    @Override
    public String getName() {
        return "channel.update";
    }

    @Override
    public String getVersion() {
        return "beta";
    }

    @Override
    public ChannelUpdateCondition.ChannelUpdateConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelUpdateCondition.builder();
    }

    @Override
    public Class<BetaChannelUpdateEvent> getEventClass() {
        return BetaChannelUpdateEvent.class;
    }

}
