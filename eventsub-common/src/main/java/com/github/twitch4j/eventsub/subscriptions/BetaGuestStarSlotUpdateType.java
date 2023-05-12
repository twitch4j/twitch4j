package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.GuestStarSlotUpdateCondition;
import com.github.twitch4j.eventsub.events.GuestStarSlotUpdateEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * Notifies when a slot setting is updated in an active Guest Star session.
 * <p>
 * Must have channel:read:guest_star, channel:manage:guest_star, moderator:read:guest_star or moderator:manage:guest_star scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_GUEST_STAR_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_GUEST_STAR_MANAGE
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_GUEST_STAR_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_GUEST_STAR_MANAGE
 */
@ApiStatus.Experimental // in open beta
public class BetaGuestStarSlotUpdateType implements SubscriptionType<GuestStarSlotUpdateCondition, GuestStarSlotUpdateCondition.GuestStarSlotUpdateConditionBuilder<?, ?>, GuestStarSlotUpdateEvent> {

    @Override
    public String getName() {
        return "channel.guest_star_slot.update";
    }

    @Override
    public String getVersion() {
        return "beta";
    }

    @Override
    public GuestStarSlotUpdateCondition.GuestStarSlotUpdateConditionBuilder<?, ?> getConditionBuilder() {
        return GuestStarSlotUpdateCondition.builder();
    }

    @Override
    public Class<GuestStarSlotUpdateEvent> getEventClass() {
        return GuestStarSlotUpdateEvent.class;
    }

}
