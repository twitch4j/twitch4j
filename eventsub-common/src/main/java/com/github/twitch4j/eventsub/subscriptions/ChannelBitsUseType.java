package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition;
import com.github.twitch4j.eventsub.events.ChannelBitsUseEvent;

/**
 * Fires when Bits are used on a channel, including Cheers, Power-ups, and Combos.
 * <p>
 * Bits transactions via Twitch Extensions are not included in this subscription type.
 * <p>
 * Authorization: Requires a user access token from the broadcaster that includes the bits:read scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_BITS_READ
 */
public class ChannelBitsUseType implements SubscriptionType<ChannelEventSubCondition, ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?>, ChannelBitsUseEvent> {
    @Override
    public String getName() {
        return "channel.bits.use";
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
    public Class<ChannelBitsUseEvent> getEventClass() {
        return ChannelBitsUseEvent.class;
    }
}
