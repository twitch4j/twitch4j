package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPollEndCondition;
import com.github.twitch4j.eventsub.events.ChannelPollEndEvent;

/**
 * The channel.poll.end subscription type sends a notification when a poll ends on the specified channel.
 * <p>
 * Must have the channel:read:polls or channel:manage:polls scope.
 */
public class PollEndType implements SubscriptionType<ChannelPollEndCondition, ChannelPollEndCondition.ChannelPollEndConditionBuilder<?, ?>, ChannelPollEndEvent> {

    @Override
    public String getName() {
        return "channel.poll.end";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelPollEndCondition.ChannelPollEndConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelPollEndCondition.builder();
    }

    @Override
    public Class<ChannelPollEndEvent> getEventClass() {
        return ChannelPollEndEvent.class;
    }

}
