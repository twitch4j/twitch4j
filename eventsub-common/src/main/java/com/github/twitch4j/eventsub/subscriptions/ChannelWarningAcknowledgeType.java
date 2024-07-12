package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ModeratorEventSubCondition;
import com.github.twitch4j.eventsub.events.ChannelWarningAcknowledgeEvent;

/**
 * Sends a notification when a warning is acknowledged by a user.
 * Broadcasters and moderators can see the warning’s details.
 * <p>
 * Must have the moderator:read:warnings or moderator:manage:warnings scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_WARNINGS_MANAGE
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_WARNINGS_READ
 */
public class ChannelWarningAcknowledgeType implements SubscriptionType<ModeratorEventSubCondition, ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?>, ChannelWarningAcknowledgeEvent> {
    @Override
    public String getName() {
        return "channel.warning.acknowledge";
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
    public Class<ChannelWarningAcknowledgeEvent> getEventClass() {
        return ChannelWarningAcknowledgeEvent.class;
    }
}
