package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.UserEventSubCondition;
import com.github.twitch4j.eventsub.events.WhisperReceivedEvent;

/**
 * Fires when anyone whispers the specified user.
 * <p>
 * Must have oauth scope user:read:whispers or user:manage:whispers.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_USER_WHISPERS_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_USER_WHISPERS_MANAGE
 */
public class WhisperReceivedType implements SubscriptionType<UserEventSubCondition, UserEventSubCondition.UserEventSubConditionBuilder<?, ?>, WhisperReceivedEvent> {
    @Override
    public String getName() {
        return "user.whisper.message";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public UserEventSubCondition.UserEventSubConditionBuilder<?, ?> getConditionBuilder() {
        return UserEventSubCondition.builder();
    }

    @Override
    public Class<WhisperReceivedEvent> getEventClass() {
        return WhisperReceivedEvent.class;
    }
}
