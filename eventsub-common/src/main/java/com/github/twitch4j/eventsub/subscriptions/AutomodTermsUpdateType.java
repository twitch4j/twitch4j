package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ModeratorEventSubCondition;
import com.github.twitch4j.eventsub.events.ChannelTermsUpdateEvent;

/**
 * Fires when a broadcaster’s AutoMod terms are updated.
 * Changes to private terms are not sent.
 * <p>
 * Requires authorization from a moderator of the channel with the moderator:manage:automod scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_AUTOMOD_MANAGE
 */
public class AutomodTermsUpdateType implements SubscriptionType<ModeratorEventSubCondition, ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?>, ChannelTermsUpdateEvent> {
    @Override
    public String getName() {
        return "automod.terms.update";
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
    public Class<ChannelTermsUpdateEvent> getEventClass() {
        return ChannelTermsUpdateEvent.class;
    }
}
