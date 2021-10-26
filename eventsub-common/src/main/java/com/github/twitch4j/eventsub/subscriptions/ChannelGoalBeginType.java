package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.CreatorGoalsCondition;
import com.github.twitch4j.eventsub.events.CreatorGoalsBeginEvent;

/**
 * Notifies the subscriber when the specified broadcaster begins a goal.
 * <p>
 * NOTE: It’s possible to receive the Begin event after receiving Progress events.
 * <p>
 * Authorization: Requires a user OAuth access token with scope set to channel:read:goals.
 */
public class ChannelGoalBeginType implements SubscriptionType<CreatorGoalsCondition, CreatorGoalsCondition.CreatorGoalsConditionBuilder<?, ?>, CreatorGoalsBeginEvent> {

    @Override
    public String getName() {
        return "channel.goal.begin";
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
    public Class<CreatorGoalsBeginEvent> getEventClass() {
        return CreatorGoalsBeginEvent.class;
    }

}
