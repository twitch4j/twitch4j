package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelModeratorRemoveCondition;
import com.github.twitch4j.eventsub.events.ChannelModeratorRemoveEvent;

/**
 * The channel.moderator.remove subscription type sends a notification when a user has moderator privileges removed on a specified channel.
 * <p>
 * Must have moderation:read scope.
 */
public class ChannelModeratorRemoveType implements SubscriptionType<ChannelModeratorRemoveCondition, ChannelModeratorRemoveCondition.ChannelModeratorRemoveConditionBuilder<?, ?>, ChannelModeratorRemoveEvent> {

    @Override
    public String getName() {
        return "channel.moderator.remove";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelModeratorRemoveCondition.ChannelModeratorRemoveConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelModeratorRemoveCondition.builder();
    }

    @Override
    public Class<ChannelModeratorRemoveEvent> getEventClass() {
        return ChannelModeratorRemoveEvent.class;
    }

}
