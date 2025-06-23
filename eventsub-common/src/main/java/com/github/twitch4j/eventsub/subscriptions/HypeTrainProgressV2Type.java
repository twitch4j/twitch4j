package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition;
import com.github.twitch4j.eventsub.events.HypeTrainProgressV2Event;
import org.jetbrains.annotations.ApiStatus;

/**
 * The channel.hype_train.progress subscription type sends a notification when a Hype Train makes progress on the specified channel. channel.hype_train.progress notifications are sent periodically while a Hype Train is making progress. EventSub does not make strong assurances about the order of message delivery, so it is possible to receive channel.hype_train.progress before you receive the corresponding channel.hype_train.begin.
 * <p>
 * When the Hype Train is over, channel.hype_train.end is emitted.
 * <p>
 * Authorization: Must have channel:read:hype_train scope.
 */
@ApiStatus.Experimental // in open beta
public class HypeTrainProgressV2Type implements SubscriptionType<ChannelEventSubCondition, ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?>, HypeTrainProgressV2Event> {

    @Override
    public String getName() {
        return "channel.hype_train.progress";
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
    public Class<HypeTrainProgressV2Event> getEventClass() {
        return HypeTrainProgressV2Event.class;
    }

}
