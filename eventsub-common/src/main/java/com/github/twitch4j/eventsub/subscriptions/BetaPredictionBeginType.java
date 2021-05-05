package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.condition.ChannelPredictionBeginCondition;
import com.github.twitch4j.eventsub.events.ChannelPredictionBeginEvent;

/**
 * The channel.prediction.begin subscription type sends a notification when a Prediction begins on the specified channel.
 * <p>
 * Must have channel:read:predictions or channel:manage:predictions scope.
 * <p>
 * Unless otherwise noted, EventSub subscriptions that were released as a public beta will be available for 30 days after their v1 version is released. Subscriptions should be updated to v1 during this timeframe.
 * Any active beta subscriptions beyond 30 days will be automatically deleted.
 */
@Unofficial
public class BetaPredictionBeginType implements SubscriptionType<ChannelPredictionBeginCondition, ChannelPredictionBeginCondition.ChannelPredictionBeginConditionBuilder<?, ?>, ChannelPredictionBeginEvent> {

    @Override
    public String getName() {
        return "channel.prediction.begin";
    }

    @Override
    public String getVersion() {
        return "beta";
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
