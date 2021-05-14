package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.condition.ChannelPredictionEndCondition;
import com.github.twitch4j.eventsub.events.ChannelPredictionEndEvent;

/**
 * The channel.prediction.end subscription type sends a notification when a Prediction ends on the specified channel.
 * <p>
 * Must have channel:read:predictions or channel:manage:predictions scope.
 * <p>
 * Unless otherwise noted, EventSub subscriptions that were released as a public beta will be available for 30 days after their v1 version is released. Subscriptions should be updated to v1 during this timeframe.
 * Any active beta subscriptions beyond 30 days will be automatically deleted.
 */
@Unofficial
public class BetaPredictionEndType implements SubscriptionType<ChannelPredictionEndCondition, ChannelPredictionEndCondition.ChannelPredictionEndConditionBuilder<?, ?>, ChannelPredictionEndEvent> {

    @Override
    public String getName() {
        return "channel.prediction.end";
    }

    @Override
    public String getVersion() {
        return "beta";
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
