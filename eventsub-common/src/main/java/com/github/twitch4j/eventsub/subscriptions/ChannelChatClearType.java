package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelChatCondition;
import com.github.twitch4j.eventsub.events.ChannelChatClearEvent;

/**
 * Sends a notification when a moderator or bot clears all messages from the chat room.
 * <p>
 * Requires user:read:chat scope from chatting user.
 * If app access token used, then additionally requires user:bot scope from chatting user,
 * and either channel:bot scope from broadcaster or moderator status.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#CHAT_USER_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#CHAT_USER_BOT
 * @see com.github.twitch4j.auth.domain.TwitchScopes#CHAT_CHANNEL_BOT
 */
public class ChannelChatClearType implements SubscriptionType<ChannelChatCondition, ChannelChatCondition.ChannelChatConditionBuilder<?, ?>, ChannelChatClearEvent> {
    @Override
    public String getName() {
        return "channel.chat.clear";
    }

    @Override
    public String getVersion() {
        return "1";
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
