package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.HypeTrainEndCondition;
import com.github.twitch4j.eventsub.events.HypeTrainEndEvent;

/**
 * A hype train ends on the specified channel.
 * <p>
 * Must have channel:read:hype_train scope.
 */
public class HypeTrainEndType implements SubscriptionType<HypeTrainEndCondition, HypeTrainEndCondition.HypeTrainEndConditionBuilder<?, ?>, HypeTrainEndEvent> {

    @Override
    public String getName() {
        return "channel.hype_train.end";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public HypeTrainEndCondition.HypeTrainEndConditionBuilder<?, ?> getConditionBuilder() {
        return HypeTrainEndCondition.builder();
    }

    @Override
    public Class<HypeTrainEndEvent> getEventClass() {
        return HypeTrainEndEvent.class;
    }

}
