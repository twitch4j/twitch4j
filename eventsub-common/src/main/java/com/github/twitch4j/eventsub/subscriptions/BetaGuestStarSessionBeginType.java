package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.GuestStarSessionBeginCondition;
import com.github.twitch4j.eventsub.events.GuestStarSessionBeginEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * The host has begun a new Guest Star session.
 * <p>
 * Must have channel:read:guest_star or channel:manage:guest_star scope, from the channel owner.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_GUEST_STAR_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_GUEST_STAR_MANAGE
 */
@ApiStatus.Experimental // in open beta
public class BetaGuestStarSessionBeginType implements SubscriptionType<GuestStarSessionBeginCondition, GuestStarSessionBeginCondition.GuestStarSessionBeginConditionBuilder<?, ?>, GuestStarSessionBeginEvent> {

    @Override
    public String getName() {
        return "channel.guest_star_session.begin";
    }

    @Override
    public String getVersion() {
        return "beta";
    }

    @Override
    public GuestStarSessionBeginCondition.GuestStarSessionBeginConditionBuilder<?, ?> getConditionBuilder() {
        return GuestStarSessionBeginCondition.builder();
    }

    @Override
    public Class<GuestStarSessionBeginEvent> getEventClass() {
        return GuestStarSessionBeginEvent.class;
    }

}
