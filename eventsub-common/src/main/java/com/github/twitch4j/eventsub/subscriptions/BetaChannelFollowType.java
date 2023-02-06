package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelFollowV2Condition;
import com.github.twitch4j.eventsub.events.ChannelFollowEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * A specified channel receives a follow.
 * <p>
 * Must have {@link com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_FOLLOWERS_READ} scope;
 * the broadcaster or a moderator must have authenticated with your client_id for this scope.
 * <p>
 * Warning: you will not be able to create subscriptions with this type after 2023-02-17 (but existing subscriptions will work for a few weeks afterwards)
 * in favor of {@link ChannelFollowTypeV2}.
 *
 * @see <a href="https://discuss.dev.twitch.tv/t/follows-endpoints-and-eventsub-subscription-type-are-now-available-in-open-beta/43322">Official Announcement</a>
 */
@ApiStatus.Experimental
public class BetaChannelFollowType implements SubscriptionType<ChannelFollowV2Condition, ChannelFollowV2Condition.ChannelFollowV2ConditionBuilder<?, ?>, ChannelFollowEvent> {

    @Override
    public String getName() {
        return "channel.follow";
    }

    @Override
    public String getVersion() {
        return "beta";
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
