package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ModeratorEventSubCondition;
import com.github.twitch4j.eventsub.events.AutomodSettingsUpdateEvent;

/**
 * Fires when a broadcaster’s automod settings are updated.
 * <p>
 * Requires authorization from a moderator of the channel with the moderator:read:automod_settings scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_AUTOMOD_SETTINGS_READ
 */
public class AutomodSettingsUpdateType implements SubscriptionType<ModeratorEventSubCondition, ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?>, AutomodSettingsUpdateEvent> {
    @Override
    public String getName() {
        return "automod.settings.update";
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
    public Class<AutomodSettingsUpdateEvent> getEventClass() {
        return AutomodSettingsUpdateEvent.class;
    }
}
