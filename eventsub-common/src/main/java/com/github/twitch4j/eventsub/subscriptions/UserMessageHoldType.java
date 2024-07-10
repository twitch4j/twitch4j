package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelChatCondition;
import com.github.twitch4j.eventsub.events.UserMessageHoldEvent;

/**
 * Notifies a user if their message is caught by AutoMod.
 * <p>
 * Requires user:read:chat scope from chatting user.
 * If app access token used, then additionally requires user:bot scope from chatting user.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#CHAT_USER_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#CHAT_USER_BOT
 */
public class UserMessageHoldType implements SubscriptionType<ChannelChatCondition, ChannelChatCondition.ChannelChatConditionBuilder<?, ?>, UserMessageHoldEvent> {
    @Override
    public String getName() {
        return "channel.chat.user_message_hold";
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
    public Class<UserMessageHoldEvent> getEventClass() {
        return UserMessageHoldEvent.class;
    }
}
