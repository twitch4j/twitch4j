package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelChatCondition;
import com.github.twitch4j.eventsub.events.ChannelChatMessageDeleteEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class BetaChannelMessageDeleteType implements SubscriptionType<ChannelChatCondition, ChannelChatCondition.ChannelChatConditionBuilder<?, ?>, ChannelChatMessageDeleteEvent> {
    @Override
    public String getName() {
        return "channel.chat.message_delete";
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
    public Class<ChannelChatMessageDeleteEvent> getEventClass() {
        return ChannelChatMessageDeleteEvent.class;
    }
}
