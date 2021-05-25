package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPredictionBeginCondition;
import com.github.twitch4j.eventsub.events.ChannelPredictionBeginEvent;

/**
 * The channel.prediction.begin subscription type sends a notification when a Prediction begins on the specified channel.
 * <p>
 * Must have channel:read:predictions or channel:manage:predictions scope.
 */
public class PredictionBeginType implements SubscriptionType<ChannelPredictionBeginCondition, ChannelPredictionBeginCondition.ChannelPredictionBeginConditionBuilder<?, ?>, ChannelPredictionBeginEvent> {

    @Override
    public String getName() {
        return "channel.prediction.begin";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelPredictionBeginCondition.ChannelPredictionBeginConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelPredictionBeginCondition.builder();
    }

    @Override
    public Class<ChannelPredictionBeginEvent> getEventClass() {
        return ChannelPredictionBeginEvent.class;
    }

}
