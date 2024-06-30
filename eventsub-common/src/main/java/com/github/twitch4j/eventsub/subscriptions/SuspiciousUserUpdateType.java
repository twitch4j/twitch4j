package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ModeratorEventSubCondition;
import com.github.twitch4j.eventsub.events.SuspiciousUserUpdateEvent;

/**
 * Fires when a suspicious user's treatment has been updated.
 * <p>
 * Requires authorization from a moderator of the channel with the moderator:read:suspicious_users scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_SUSPICIOUS_USERS_READ
 */
public class SuspiciousUserUpdateType implements SubscriptionType<ModeratorEventSubCondition, ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?>, SuspiciousUserUpdateEvent> {
    @Override
    public String getName() {
        return "channel.suspicious_user.update";
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
    public Class<SuspiciousUserUpdateEvent> getEventClass() {
        return SuspiciousUserUpdateEvent.class;
    }
}
