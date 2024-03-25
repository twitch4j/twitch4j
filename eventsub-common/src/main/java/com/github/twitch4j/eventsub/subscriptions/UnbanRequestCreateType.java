package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ModeratorEventSubCondition;
import com.github.twitch4j.eventsub.events.UnbanRequestCreatedEvent;

/**
 * Fires when a user creates an unban request.
 * <p>
 * Must have moderator:read:unban_requests or moderator:manage:unban_requests scope.
 * <p>
 * If you use webhooks, the user in moderator_id must have granted your app (client ID)
 * one of the above permissions prior to your app subscribing to this subscription type.
 * <p>
 * If you use WebSockets, the ID in moderator_id must match the user ID in the user access token.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_UNBAN_REQUESTS_MANAGE
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_UNBAN_REQUESTS_READ
 */
public class UnbanRequestCreateType implements SubscriptionType<ModeratorEventSubCondition, ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?>, UnbanRequestCreatedEvent> {
    @Override
    public String getName() {
        return "channel.unban_request.create";
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
    public Class<UnbanRequestCreatedEvent> getEventClass() {
        return UnbanRequestCreatedEvent.class;
    }
}
