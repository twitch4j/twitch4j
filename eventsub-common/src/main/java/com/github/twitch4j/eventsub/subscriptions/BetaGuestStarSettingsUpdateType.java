package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.GuestStarSettingsUpdateCondition;
import com.github.twitch4j.eventsub.events.GuestStarSettingsUpdateEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * Notifies when the host preferences for Guest Star have been updated.
 * <p>
 * Must have channel:read:guest_star, channel:manage:guest_star, moderator:read:guest_star or moderator:manage:guest_star scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_GUEST_STAR_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_GUEST_STAR_MANAGE
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_GUEST_STAR_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_GUEST_STAR_MANAGE
 */
@ApiStatus.Experimental // in open beta
public class BetaGuestStarSettingsUpdateType implements SubscriptionType<GuestStarSettingsUpdateCondition, GuestStarSettingsUpdateCondition.GuestStarSettingsUpdateConditionBuilder<?, ?>, GuestStarSettingsUpdateEvent> {

    @Override
    public String getName() {
        return "channel.guest_star_settings.update";
    }

    @Override
    public String getVersion() {
        return "beta";
    }

    @Override
    public GuestStarSettingsUpdateCondition.GuestStarSettingsUpdateConditionBuilder<?, ?> getConditionBuilder() {
        return GuestStarSettingsUpdateCondition.builder();
    }

    @Override
    public Class<GuestStarSettingsUpdateEvent> getEventClass() {
        return GuestStarSettingsUpdateEvent.class;
    }

}
