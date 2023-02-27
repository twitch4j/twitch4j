package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelFollowV2Condition;
import com.github.twitch4j.eventsub.events.ChannelFollowEvent;

/**
 * A specified channel receives a follow.
 * <p>
 * Must have moderator:read:followers scope; the broadcaster or a moderator must have authenticated with your client_id for this scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_FOLLOWERS_READ
 */
public class ChannelFollowTypeV2 implements SubscriptionType<ChannelFollowV2Condition, ChannelFollowV2Condition.ChannelFollowV2ConditionBuilder<?, ?>, ChannelFollowEvent> {

    @Override
    public String getName() {
        return "channel.follow";
    }

    @Override
    public String getVersion() {
        return "2";
    }

    @Override
    public ChannelFollowV2Condition.ChannelFollowV2ConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelFollowV2Condition.builder();
    }

    @Override
    public Class<ChannelFollowEvent> getEventClass() {
        return ChannelFollowEvent.class;
    }

}
