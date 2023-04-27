package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ShieldModeCondition;
import com.github.twitch4j.eventsub.events.ShieldModeBeginEvent;

/**
 * Sends a notification when the broadcaster activates Shield Mode.
 * <p>
 * This event informs the subscriber that the broadcaster’s moderation settings were changed based on the broadcaster’s Shield Mode configuration settings.
 * <p>
 * Requires the moderator:read:shield_mode or moderator:manage:shield_mode scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_SHIELD_MODE_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_SHIELD_MODE_MANAGE
 */
public class ShieldModeBeginType implements SubscriptionType<ShieldModeCondition, ShieldModeCondition.ShieldModeConditionBuilder<?, ?>, ShieldModeBeginEvent> {
    @Override
    public String getName() {
        return "channel.shield_mode.begin";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ShieldModeCondition.ShieldModeConditionBuilder<?, ?> getConditionBuilder() {
        return ShieldModeCondition.builder();
    }

    @Override
    public Class<ShieldModeBeginEvent> getEventClass() {
        return ShieldModeBeginEvent.class;
    }
}
