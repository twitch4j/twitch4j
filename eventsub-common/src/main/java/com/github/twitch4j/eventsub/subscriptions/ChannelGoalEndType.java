package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.CreatorGoalsCondition;
import com.github.twitch4j.eventsub.events.CreatorGoalsEndEvent;

/**
 * Notifies the subscriber when the specified broadcaster ends a goal.
 * <p>
 * Authorization: Requires a user OAuth access token with scope set to channel:read:goals.
 */
public class ChannelGoalEndType implements SubscriptionType<CreatorGoalsCondition, CreatorGoalsCondition.CreatorGoalsConditionBuilder<?, ?>, CreatorGoalsEndEvent> {

    @Override
    public String getName() {
        return "channel.goal.end";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public CreatorGoalsCondition.CreatorGoalsConditionBuilder<?, ?> getConditionBuilder() {
        return CreatorGoalsCondition.builder();
    }

    @Override
    public Class<CreatorGoalsEndEvent> getEventClass() {
        return CreatorGoalsEndEvent.class;
    }

}
