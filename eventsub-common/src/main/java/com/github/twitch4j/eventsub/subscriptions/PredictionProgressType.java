package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPredictionProgressCondition;
import com.github.twitch4j.eventsub.events.ChannelPredictionProgressEvent;

/**
 * The channel.prediction.progress subscription type sends a notification when users participate in a Prediction on the specified channel.
 * <p>
 * Must have channel:read:predictions or channel:manage:predictions scope.
 */
public class PredictionProgressType implements SubscriptionType<ChannelPredictionProgressCondition, ChannelPredictionProgressCondition.ChannelPredictionProgressConditionBuilder<?, ?>, ChannelPredictionProgressEvent> {

    @Override
    public String getName() {
        return "channel.prediction.progress";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelPredictionProgressCondition.ChannelPredictionProgressConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelPredictionProgressCondition.builder();
    }

    @Override
    public Class<ChannelPredictionProgressEvent> getEventClass() {
        return ChannelPredictionProgressEvent.class;
    }

}
