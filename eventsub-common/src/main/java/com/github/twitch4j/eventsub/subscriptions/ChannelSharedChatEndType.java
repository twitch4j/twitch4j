package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition;
import com.github.twitch4j.eventsub.events.ChannelSharedChatEndEvent;

/**
 * Fires when a channel leaves a shared chat session or the session ends.
 * <p>
 * Authorization: No authorization required.
 */
public class ChannelSharedChatEndType implements SubscriptionType<ChannelEventSubCondition, ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?>, ChannelSharedChatEndEvent> {
    @Override
    public String getName() {
        return "channel.shared_chat.end";
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
    public Class<ChannelSharedChatEndEvent> getEventClass() {
        return ChannelSharedChatEndEvent.class;
    }
}
