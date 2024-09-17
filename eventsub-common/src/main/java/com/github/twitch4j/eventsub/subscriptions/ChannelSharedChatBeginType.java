package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition;
import com.github.twitch4j.eventsub.events.ChannelSharedChatBeginEvent;

/**
 * Fires when a channel becomes active in an active shared chat session.
 * <p>
 * Authorization: No authorization required.
 */
public class ChannelSharedChatBeginType implements SubscriptionType<ChannelEventSubCondition, ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?>, ChannelSharedChatBeginEvent> {
    @Override
    public String getName() {
        return "channel.shared_chat.begin";
    }

    @Override
    public String getVersion() {
        return "1";
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
