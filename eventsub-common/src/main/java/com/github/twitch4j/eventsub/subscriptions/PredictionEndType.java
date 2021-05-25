package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPredictionEndCondition;
import com.github.twitch4j.eventsub.events.ChannelPredictionEndEvent;

/**
 * The channel.prediction.end subscription type sends a notification when a Prediction ends on the specified channel.
 * <p>
 * Must have channel:read:predictions or channel:manage:predictions scope.
 */
public class PredictionEndType implements SubscriptionType<ChannelPredictionEndCondition, ChannelPredictionEndCondition.ChannelPredictionEndConditionBuilder<?, ?>, ChannelPredictionEndEvent> {

    @Override
    public String getName() {
        return "channel.prediction.end";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelPredictionEndCondition.ChannelPredictionEndConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelPredictionEndCondition.builder();
    }

    @Override
    public Class<ChannelPredictionEndEvent> getEventClass() {
        return ChannelPredictionEndEvent.class;
    }

}
