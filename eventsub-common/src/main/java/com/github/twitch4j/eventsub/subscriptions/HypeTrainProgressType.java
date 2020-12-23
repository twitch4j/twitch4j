package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.HypeTrainProgressCondition;
import com.github.twitch4j.eventsub.events.HypeTrainProgressEvent;

/**
 * A hype train makes progress on the specified channel.
 * <p>
 * Must have channel:read:hype_train scope.
 * <p>
 * EventSub does not make strong assurances about the order of message delivery, so it is possible to receive channel.hype_train.progress before you receive the corresponding channel.hype_train.begin
 */
public class HypeTrainProgressType implements SubscriptionType<HypeTrainProgressCondition, HypeTrainProgressCondition.HypeTrainProgressConditionBuilder<?, ?>, HypeTrainProgressEvent> {

    @Override
    public String getName() {
        return "channel.hype_train.progress";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public HypeTrainProgressCondition.HypeTrainProgressConditionBuilder<?, ?> getConditionBuilder() {
        return HypeTrainProgressCondition.builder();
    }

    @Override
    public Class<HypeTrainProgressEvent> getEventClass() {
        return HypeTrainProgressEvent.class;
    }

}
