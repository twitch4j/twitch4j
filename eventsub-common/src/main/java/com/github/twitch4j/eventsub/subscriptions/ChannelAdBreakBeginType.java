package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelAdBreakCondition;
import com.github.twitch4j.eventsub.events.ChannelAdBreakBeginEvent;

/**
 * The channel.ad_break.begin subscription type sends a notification when a user runs a midroll commercial break,
 * either manually or automatically via ads manager.
 * <p>
 * Must have channel:read:ads scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_ADS_READ
 */
public class ChannelAdBreakBeginType implements SubscriptionType<ChannelAdBreakCondition, ChannelAdBreakCondition.ChannelAdBreakConditionBuilder<?, ?>, ChannelAdBreakBeginEvent> {
    @Override
    public String getName() {
        return "channel.ad_break.begin";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelAdBreakCondition.ChannelAdBreakConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelAdBreakCondition.builder();
    }

    @Override
    public Class<ChannelAdBreakBeginEvent> getEventClass() {
        return ChannelAdBreakBeginEvent.class;
    }
}
