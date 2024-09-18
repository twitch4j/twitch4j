package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition;
import com.github.twitch4j.eventsub.events.ChannelSharedChatUpdateEvent;

/**
 * Fires when the active shared chat session the channel is in changes.
 * <p>
 * Authorization: No authorization required.
 */
public class ChannelSharedChatUpdateType implements SubscriptionType<ChannelEventSubCondition, ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?>, ChannelSharedChatUpdateEvent> {
    @Override
    public String getName() {
        return "channel.shared_chat.update";
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
    public Class<ChannelSharedChatUpdateEvent> getEventClass() {
        return ChannelSharedChatUpdateEvent.class;
    }
}
