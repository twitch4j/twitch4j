package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelFollowCondition;
import com.github.twitch4j.eventsub.events.ChannelFollowEvent;

/**
 * A specified channel receives a follow.
 * <p>
 * No authorization required.
 *
 * @see <a href="https://discuss.dev.twitch.tv/t/follows-endpoints-and-eventsub-subscription-type-are-now-available-in-open-beta/43322">Deprecation Announcement</a>
 * @deprecated Without prior notice, Twitch has restricted this subscription to client_id's that were using it <i>on</i> 2023-02-17.
 * Furthermore, Twitch will shutdown this topic on <a href="https://discuss.dev.twitch.tv/t/legacy-follows-api-and-eventsub-shutdown-timeline-updated">2023-09-12</a>
 * in favor of {@link ChannelFollowTypeV2} (which has the same event data but more stringent auth).
 */
@Deprecated
@SuppressWarnings("DeprecatedIsStillUsed")
public class ChannelFollowType implements SubscriptionType<ChannelFollowCondition, ChannelFollowCondition.ChannelFollowConditionBuilder<?, ?>, ChannelFollowEvent> {

    @Override
    public String getName() {
        return "channel.follow";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelFollowCondition.ChannelFollowConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelFollowCondition.builder();
    }

    @Override
    public Class<ChannelFollowEvent> getEventClass() {
        return ChannelFollowEvent.class;
    }

}
