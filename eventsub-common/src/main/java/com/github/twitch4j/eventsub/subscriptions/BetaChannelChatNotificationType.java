package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelChatCondition;
import com.github.twitch4j.eventsub.events.ChannelChatNotificationEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * Sends a notification when a USERNOTICE event that appears in chat occurs,
 * such as someone subscribing to the channel or a subscription is gifted.
 * <p>
 * Requires user:read:chat scope from chatting user.
 * If app access token used, then additionally requires user:bot scope from chatting user,
 * and either channel:bot scope from broadcaster or moderator status.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#CHAT_USER_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#CHAT_USER_BOT
 * @see com.github.twitch4j.auth.domain.TwitchScopes#CHAT_CHANNEL_BOT
 */
@ApiStatus.Experimental // in open beta
public class BetaChannelChatNotificationType implements SubscriptionType<ChannelChatCondition, ChannelChatCondition.ChannelChatConditionBuilder<?, ?>, ChannelChatNotificationEvent> {
    @Override
    public String getName() {
        return "channel.chat.notification";
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
    public Class<ChannelChatNotificationEvent> getEventClass() {
        return ChannelChatNotificationEvent.class;
    }
}