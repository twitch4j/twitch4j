package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelChatCondition;
import com.github.twitch4j.eventsub.events.ChannelChatClearUserMessagesEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class BetaChannelClearUserMessagesType implements SubscriptionType<ChannelChatCondition, ChannelChatCondition.ChannelChatConditionBuilder<?, ?>, ChannelChatClearUserMessagesEvent> {
    @Override
    public String getName() {
        return "channel.chat.clear_user_messages";
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
    public Class<ChannelChatClearUserMessagesEvent> getEventClass() {
        return ChannelChatClearUserMessagesEvent.class;
    }
}
