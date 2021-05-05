package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.condition.ChannelPredictionProgressCondition;
import com.github.twitch4j.eventsub.events.ChannelPredictionProgressEvent;

/**
 * The channel.prediction.progress subscription type sends a notification when users participate in a Prediction on the specified channel.
 * <p>
 * Must have channel:read:predictions or channel:manage:predictions scope.
 * <p>
 * Unless otherwise noted, EventSub subscriptions that were released as a public beta will be available for 30 days after their v1 version is released. Subscriptions should be updated to v1 during this timeframe.
 * Any active beta subscriptions beyond 30 days will be automatically deleted.
 */
@Unofficial
public class BetaPredictionProgressType implements SubscriptionType<ChannelPredictionProgressCondition, ChannelPredictionProgressCondition.ChannelPredictionProgressConditionBuilder<?, ?>, ChannelPredictionProgressEvent> {

    @Override
    public String getName() {
        return "channel.prediction.progress";
    }

    @Override
    public String getVersion() {
        return "beta";
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
