package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ModeratorEventSubCondition;
import com.github.twitch4j.eventsub.events.SuspiciousUserMessageEvent;

/**
 * Fires when a chat message has been sent from a suspicious user.
 * <p>
 * Requires authorization from a moderator of the channel with the moderator:read:suspicious_users scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_SUSPICIOUS_USERS_READ
 */
public class SuspiciousUserMessageType implements SubscriptionType<ModeratorEventSubCondition, ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?>, SuspiciousUserMessageEvent> {
    @Override
    public String getName() {
        return "channel.suspicious_user.message";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?> getConditionBuilder() {
        return ModeratorEventSubCondition.builder();
    }

    @Override
    public Class<SuspiciousUserMessageEvent> getEventClass() {
        return SuspiciousUserMessageEvent.class;
    }
}
