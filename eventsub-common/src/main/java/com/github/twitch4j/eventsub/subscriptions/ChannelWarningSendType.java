package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ModeratorEventSubCondition;
import com.github.twitch4j.eventsub.events.ChannelWarningSendEvent;

/**
 * Sends a notification when a warning is sent to a user.
 * Broadcasters and moderators can see the warning’s details.
 * <p>
 * Must have the moderator:read:warnings or moderator:manage:warnings scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_WARNINGS_MANAGE
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_WARNINGS_READ
 */
public class ChannelWarningSendType implements SubscriptionType<ModeratorEventSubCondition, ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?>, ChannelWarningSendEvent> {
    @Override
    public String getName() {
        return "channel.warning.send";
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
    public Class<ChannelWarningSendEvent> getEventClass() {
        return ChannelWarningSendEvent.class;
    }
}
