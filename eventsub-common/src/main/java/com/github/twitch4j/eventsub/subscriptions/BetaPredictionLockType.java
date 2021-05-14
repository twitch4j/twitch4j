package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.condition.ChannelPredictionLockCondition;
import com.github.twitch4j.eventsub.events.ChannelPredictionLockEvent;

/**
 * The channel.prediction.lock subscription type sends a notification when a Prediction is locked on the specified channel.
 * <p>
 * Must have channel:read:predictions or channel:manage:predictions scope.
 * <p>
 * Unless otherwise noted, EventSub subscriptions that were released as a public beta will be available for 30 days after their v1 version is released. Subscriptions should be updated to v1 during this timeframe.
 * Any active beta subscriptions beyond 30 days will be automatically deleted.
 */
@Unofficial
public class BetaPredictionLockType implements SubscriptionType<ChannelPredictionLockCondition, ChannelPredictionLockCondition.ChannelPredictionLockConditionBuilder<?, ?>, ChannelPredictionLockEvent> {

    @Override
    public String getName() {
        return "channel.prediction.lock";
    }

    @Override
    public String getVersion() {
        return "beta";
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
