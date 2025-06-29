package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition;
import com.github.twitch4j.eventsub.events.HypeTrainBeginV2Event;
import org.jetbrains.annotations.ApiStatus;

/**
 * The channel.hype_train.begin subscription type sends a notification when a Hype Train begins on the specified channel.
 * <p>
 * In addition to a channel.hype_train.begin event, one channel.hype_train.progress event will be sent for each contribution that caused the Hype Train to begin.
 * <p>
 * EventSub does not make strong assurances about the order of message delivery, so it is possible to receive
 * channel.hype_train.progress notifications before you receive the corresponding channel.hype_train.begin notification.
 * <p>
 * After the Hype Train begins, any additional cheers or subscriptions on the channel will cause channel.hype_train.progress notifications to be sent.
 * When the Hype Train is over, channel.hype_train.end is emitted.
 * <p>
 * Authorization: Must have channel:read:hype_train scope (from the broadcaster).
 */
@ApiStatus.Experimental // in open beta
public class HypeTrainBeginV2Type implements SubscriptionType<ChannelEventSubCondition, ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?>, HypeTrainBeginV2Event> {

    @Override
    public String getName() {
        return "channel.hype_train.begin";
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
    public Class<HypeTrainBeginV2Event> getEventClass() {
        return HypeTrainBeginV2Event.class;
    }

}
