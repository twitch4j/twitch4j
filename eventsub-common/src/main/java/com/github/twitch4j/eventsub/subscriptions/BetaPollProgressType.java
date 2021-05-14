package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.condition.ChannelPollProgressCondition;
import com.github.twitch4j.eventsub.events.ChannelPollProgressEvent;

/**
 * The channel.poll.progress subscription type sends a notification when users respond to a poll on the specified channel.
 * <p>
 * Must have the channel:read:polls or channel:manage:polls scope.
 * <p>
 * Unless otherwise noted, EventSub subscriptions that were released as a public beta will be available for 30 days after their v1 version is released. Subscriptions should be updated to v1 during this timeframe.
 * Any active beta subscriptions beyond the 30 days will be automatically deleted.
 */
@Unofficial
public class BetaPollProgressType implements SubscriptionType<ChannelPollProgressCondition, ChannelPollProgressCondition.ChannelPollProgressConditionBuilder<?, ?>, ChannelPollProgressEvent> {

    @Override
    public String getName() {
        return "channel.poll.progress";
    }

    @Override
    public String getVersion() {
        return "beta";
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
