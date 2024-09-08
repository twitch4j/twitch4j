package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition;
import com.github.twitch4j.eventsub.events.ChannelSharedChatBeginEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fires when a channel becomes active in an active shared chat session.
 * <p>
 * Authorization: No authorization required.
 */
@ApiStatus.Experimental // in open beta
public class BetaChannelSharedChatBeginType implements SubscriptionType<ChannelEventSubCondition, ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?>, ChannelSharedChatBeginEvent> {
    @Override
    public String getName() {
        return "channel.shared_chat.begin";
    }

    @Override
    public String getVersion() {
        return "beta";
    }

    @Override
    public ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelEventSubCondition.builder();
    }

    @Override
    public Class<ChannelSharedChatBeginEvent> getEventClass() {
        return ChannelSharedChatBeginEvent.class;
    }
}
