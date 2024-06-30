package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition;
import com.github.twitch4j.eventsub.events.ChannelVipRemoveEvent;

/**
 * This subscription type sends a notification when a VIP is removed from the channel.
 * <p>
 * Must have channel:read:vips or channel:manage:vips scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_VIPS_MANAGE
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_VIPS_READ
 */
public class ChannelVipRemoveType implements SubscriptionType<ChannelEventSubCondition, ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?>, ChannelVipRemoveEvent> {
    @Override
    public String getName() {
        return "channel.vip.remove";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelEventSubCondition.builder();
    }

    @Override
    public Class<ChannelVipRemoveEvent> getEventClass() {
        return ChannelVipRemoveEvent.class;
    }
}
