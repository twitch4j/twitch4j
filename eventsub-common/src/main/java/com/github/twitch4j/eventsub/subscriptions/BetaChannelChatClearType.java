package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelChatCondition;
import com.github.twitch4j.eventsub.events.ChannelChatClearEvent;

public class BetaChannelChatClearType implements SubscriptionType<ChannelChatCondition, ChannelChatCondition.ChannelChatConditionBuilder<?, ?>, ChannelChatClearEvent> {
    @Override
    public String getName() {
        return "channel.chat.clear";
    }

    @Override
    public String getVersion() {
        return "beta";
    }

    @Override
    public ChannelChatCondition.ChannelChatConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelChatCondition.builder();
    }

    @Override
    public Class<ChannelChatClearEvent> getEventClass() {
        return ChannelChatClearEvent.class;
    }
}
