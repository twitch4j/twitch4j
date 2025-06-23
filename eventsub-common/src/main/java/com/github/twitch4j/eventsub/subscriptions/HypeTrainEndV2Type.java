package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition;
import com.github.twitch4j.eventsub.events.HypeTrainEndV2Event;
import org.jetbrains.annotations.ApiStatus;

/**
 * The channel.hype_train.end subscription type sends a notification when a Hype Train ends on the specified channel.
 * <p>
 * Authorization: Must have channel:read:hype_train scope.
 */
@ApiStatus.Experimental // in open beta
public class HypeTrainEndV2Type implements SubscriptionType<ChannelEventSubCondition, ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?>, HypeTrainEndV2Event> {

    @Override
    public String getName() {
        return "channel.hype_train.end";
    }

    @Override
    public String getVersion() {
        return "2";
    }

    @Override
    public ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelEventSubCondition.builder();
    }

    @Override
    public Class<HypeTrainEndV2Event> getEventClass() {
        return HypeTrainEndV2Event.class;
    }

}
