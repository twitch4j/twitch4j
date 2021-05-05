package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.condition.ChannelPollEndCondition;
import com.github.twitch4j.eventsub.events.ChannelPollEndEvent;

/**
 * The channel.poll.end subscription type sends a notification when a poll ends on the specified channel.
 * <p>
 * Must have the channel:read:polls or channel:manage:polls scope.
 * <p>
 * Unless otherwise noted, EventSub subscriptions that were released as a public beta will be available for 30 days after their v1 version is released. Subscriptions should be updated to v1 during this timeframe.
 * Any active beta subscriptions beyond the 30 days will be automatically deleted.
 */
@Unofficial
public class BetaPollEndType implements SubscriptionType<ChannelPollEndCondition, ChannelPollEndCondition.ChannelPollEndConditionBuilder<?, ?>, ChannelPollEndEvent> {

    @Override
    public String getName() {
        return "channel.poll.end";
    }

    @Override
    public String getVersion() {
        return "beta";
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
