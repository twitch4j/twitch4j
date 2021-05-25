package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPollBeginCondition;
import com.github.twitch4j.eventsub.events.ChannelPollBeginEvent;

/**
 * The channel.poll.begin subscription type sends a notification when a poll begins on the specified channel.
 * <p>
 * Must have the channel:read:polls or channel:manage:polls scope.
 */
public class PollBeginType implements SubscriptionType<ChannelPollBeginCondition, ChannelPollBeginCondition.ChannelPollBeginConditionBuilder<?, ?>, ChannelPollBeginEvent> {

    @Override
    public String getName() {
        return "channel.poll.begin";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelPollBeginCondition.ChannelPollBeginConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelPollBeginCondition.builder();
    }

    @Override
    public Class<ChannelPollBeginEvent> getEventClass() {
        return ChannelPollBeginEvent.class;
    }

}
