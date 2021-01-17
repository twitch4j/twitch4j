package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.HypeTrainBeginCondition;
import com.github.twitch4j.eventsub.events.HypeTrainBeginEvent;

/**
 * A hype train begins on the specified channel.
 * <p>
 * Must have channel:read:hype_train scope.
 * <p>
 * EventSub does not make strong assurances about the order of message delivery, so it is possible to receive progress notifications before you receive the corresponding begin notification.
 */
public class HypeTrainBeginType implements SubscriptionType<HypeTrainBeginCondition, HypeTrainBeginCondition.HypeTrainBeginConditionBuilder<?, ?>, HypeTrainBeginEvent> {

    @Override
    public String getName() {
        return "channel.hype_train.begin";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public HypeTrainBeginCondition.HypeTrainBeginConditionBuilder<?, ?> getConditionBuilder() {
        return HypeTrainBeginCondition.builder();
    }

    @Override
    public Class<HypeTrainBeginEvent> getEventClass() {
        return HypeTrainBeginEvent.class;
    }

}
