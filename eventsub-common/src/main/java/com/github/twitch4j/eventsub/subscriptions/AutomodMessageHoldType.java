package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ModeratorEventSubCondition;
import com.github.twitch4j.eventsub.events.AutomodMessageHoldEvent;

/**
 * Notifies moderators when AutoMod held a user's message for review.
 * <p>
 * Requires a user access token that includes the moderator:manage:automod scope.
 * The ID in the moderator_user_id condition parameter must match the user ID in the access token.
 * If app access token used, then additionally requires the moderator:manage:automod scope for the moderator.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_AUTOMOD_MANAGE
 */
public class AutomodMessageHoldType implements SubscriptionType<ModeratorEventSubCondition, ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?>, AutomodMessageHoldEvent> {
    @Override
    public String getName() {
        return "automod.message.hold";
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
    public Class<AutomodMessageHoldEvent> getEventClass() {
        return AutomodMessageHoldEvent.class;
    }
}
