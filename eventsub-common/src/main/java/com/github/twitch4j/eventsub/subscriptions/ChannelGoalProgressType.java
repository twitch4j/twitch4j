package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.CreatorGoalsCondition;
import com.github.twitch4j.eventsub.events.CreatorGoalsProgressEvent;

/**
 * Notifies the subscriber when progress is made towards the specified broadcaster’s goal.
 * Progress could be positive (added followers) or negative (lost followers).
 * <p>
 * NOTE: It’s possible to receive Progress events before receiving the Begin event.
 * <p>
 * Authorization: Requires a user OAuth access token with scope set to channel:read:goals.
 */
public class ChannelGoalProgressType implements SubscriptionType<CreatorGoalsCondition, CreatorGoalsCondition.CreatorGoalsConditionBuilder<?, ?>, CreatorGoalsProgressEvent> {

    @Override
    public String getName() {
        return "channel.goal.progress";
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
    public Class<CreatorGoalsProgressEvent> getEventClass() {
        return CreatorGoalsProgressEvent.class;
    }

}
