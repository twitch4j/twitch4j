package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelModeratorAddCondition;
import com.github.twitch4j.eventsub.events.ChannelModeratorAddEvent;

/**
 * The channel.moderator.add subscription type sends a notification when a user is given moderator privileges on a specified channel.
 * <p>
 * Must have moderation:read scope.
 */
public class ChannelModeratorAddType implements SubscriptionType<ChannelModeratorAddCondition, ChannelModeratorAddCondition.ChannelModeratorAddConditionBuilder<?, ?>, ChannelModeratorAddEvent> {

    @Override
    public String getName() {
        return "channel.moderator.add";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelModeratorAddCondition.ChannelModeratorAddConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelModeratorAddCondition.builder();
    }

    @Override
    public Class<ChannelModeratorAddEvent> getEventClass() {
        return ChannelModeratorAddEvent.class;
    }

}
