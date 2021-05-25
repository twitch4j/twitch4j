package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelPollProgressCondition;
import com.github.twitch4j.eventsub.events.ChannelPollProgressEvent;

/**
 * The channel.poll.progress subscription type sends a notification when users respond to a poll on the specified channel.
 * <p>
 * Must have the channel:read:polls or channel:manage:polls scope.
 */
public class PollProgressType implements SubscriptionType<ChannelPollProgressCondition, ChannelPollProgressCondition.ChannelPollProgressConditionBuilder<?, ?>, ChannelPollProgressEvent> {

    @Override
    public String getName() {
        return "channel.poll.progress";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelPollProgressCondition.ChannelPollProgressConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelPollProgressCondition.builder();
    }

    @Override
    public Class<ChannelPollProgressEvent> getEventClass() {
        return ChannelPollProgressEvent.class;
    }

}
