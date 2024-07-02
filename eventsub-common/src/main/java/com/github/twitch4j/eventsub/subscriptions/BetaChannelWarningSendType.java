package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ModeratorEventSubCondition;
import com.github.twitch4j.eventsub.events.ChannelWarningSendEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * Sends a notification when a warning is sent to a user.
 * Broadcasters and moderators can see the warning’s details.
 * <p>
 * Must have the moderator:read:warnings or moderator:manage:warnings scope.
 *
 * @apiNote This topic is in public beta, and could break without notice.
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_WARNINGS_MANAGE
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_WARNINGS_READ
 */
@ApiStatus.Experimental
public class BetaChannelWarningSendType implements SubscriptionType<ModeratorEventSubCondition, ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?>, ChannelWarningSendEvent> {
    @Override
    public String getName() {
        return "channel.warning.send";
    }

    @Override
    public String getVersion() {
        return "beta";
    }

    @Override
    public ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?> getConditionBuilder() {
        return ModeratorEventSubCondition.builder();
    }

    @Override
    public Class<ChannelWarningSendEvent> getEventClass() {
        return ChannelWarningSendEvent.class;
    }
}
