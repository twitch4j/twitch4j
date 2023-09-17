package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.GuestStarSessionEndCondition;
import com.github.twitch4j.eventsub.events.GuestStarSessionEndEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * The running Guest Star session is ended by the host, or automatically by the system.
 * <p>
 * Must have channel:read:guest_star, channel:manage:guest_star,
 * moderator:read:guest_star or moderator:manage:guest_star
 * scope from the channel owner or a guest star moderator.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_GUEST_STAR_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_GUEST_STAR_MANAGE
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_GUEST_STAR_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_GUEST_STAR_MANAGE
 */
@ApiStatus.Experimental // in open beta
public class BetaGuestStarSessionEndType implements SubscriptionType<GuestStarSessionEndCondition, GuestStarSessionEndCondition.GuestStarSessionEndConditionBuilder<?, ?>, GuestStarSessionEndEvent> {

    @Override
    public String getName() {
        return "channel.guest_star_session.end";
    }

    @Override
    public String getVersion() {
        return "beta";
    }

    @Override
    public GuestStarSessionEndCondition.GuestStarSessionEndConditionBuilder<?, ?> getConditionBuilder() {
        return GuestStarSessionEndCondition.builder();
    }

    @Override
    public Class<GuestStarSessionEndEvent> getEventClass() {
        return GuestStarSessionEndEvent.class;
    }

}
