package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.condition.ChannelPollBeginCondition;
import com.github.twitch4j.eventsub.events.ChannelPollBeginEvent;

/**
 * The channel.poll.begin subscription type sends a notification when a poll begins on the specified channel.
 * <p>
 * Must have the channel:read:polls or channel:manage:polls scope.
 * <p>
 * Unless otherwise noted, EventSub subscriptions that were released as a public beta will be available for 30 days after their v1 version is released. Subscriptions should be updated to v1 during this timeframe.
 * Any active beta subscriptions beyond the 30 days will be automatically deleted.
 */
@Unofficial
public class BetaPollBeginType implements SubscriptionType<ChannelPollBeginCondition, ChannelPollBeginCondition.ChannelPollBeginConditionBuilder<?, ?>, ChannelPollBeginEvent> {

    @Override
    public String getName() {
        return "channel.poll.begin";
    }

    @Override
    public String getVersion() {
        return "beta";
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
