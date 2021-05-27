package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPredictionLockCondition;
import com.github.twitch4j.eventsub.events.ChannelPredictionLockEvent;

/**
 * The channel.prediction.lock subscription type sends a notification when a Prediction is locked on the specified channel.
 * <p>
 * Must have channel:read:predictions or channel:manage:predictions scope.
 */
public class PredictionLockType implements SubscriptionType<ChannelPredictionLockCondition, ChannelPredictionLockCondition.ChannelPredictionLockConditionBuilder<?, ?>, ChannelPredictionLockEvent> {

    @Override
    public String getName() {
        return "channel.prediction.lock";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelPredictionLockCondition.ChannelPredictionLockConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelPredictionLockCondition.builder();
    }

    @Override
    public Class<ChannelPredictionLockEvent> getEventClass() {
        return ChannelPredictionLockEvent.class;
    }

}
