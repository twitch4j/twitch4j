package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.GuestStarGuestUpdateCondition;
import com.github.twitch4j.eventsub.events.GuestStarGuestUpdateEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * Notifies when a guest moves between interaction states in an active Guest Star session.
 * <p>
 * Must have channel:read:guest_star, channel:manage:guest_star, moderator:read:guest_star or moderator:manage:guest_star scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_GUEST_STAR_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_GUEST_STAR_MANAGE
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_GUEST_STAR_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_GUEST_STAR_MANAGE
 */
@ApiStatus.Experimental // in open beta
public class BetaGuestStarGuestUpdateType implements SubscriptionType<GuestStarGuestUpdateCondition, GuestStarGuestUpdateCondition.GuestStarGuestUpdateConditionBuilder<?, ?>, GuestStarGuestUpdateEvent> {

    @Override
    public String getName() {
        return "channel.guest_star_guest.update";
    }

    @Override
    public String getVersion() {
        return "beta";
    }

    @Override
    public GuestStarGuestUpdateCondition.GuestStarGuestUpdateConditionBuilder<?, ?> getConditionBuilder() {
        return GuestStarGuestUpdateCondition.builder();
    }

    @Override
    public Class<GuestStarGuestUpdateEvent> getEventClass() {
        return GuestStarGuestUpdateEvent.class;
    }

}
