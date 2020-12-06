package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.HypeTrainEndCondition;

/**
 * A hype train ends on the specified channel.
 * <p>
 * Must have channel:read:hype_train scope.
 */
public class HypeTrainEndType implements SubscriptionType<HypeTrainEndCondition, HypeTrainEndCondition.HypeTrainEndConditionBuilder<?, ?>> {

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

}
