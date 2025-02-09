package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition;
import com.github.twitch4j.eventsub.events.ChannelBitsUseEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fires when Bits are used on a channel, including Cheers and Power-ups.
 * <p>
 * Bits transactions via Twitch Extensions are not included in this subscription type.
 * <p>
 * Authorization: Requires a user access token from the broadcaster that includes the bits:read scope.
 *
 * @apiNote This topic went into open beta on 2025-02-06 and this class will be removed once v1 is available.
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_BITS_READ
 */
@ApiStatus.Experimental // open beta since 2025-02-06
public class BetaChannelBitsUseType implements SubscriptionType<ChannelEventSubCondition, ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?>, ChannelBitsUseEvent> {
    @Override
    public String getName() {
        return "channel.bits.use";
    }

    @Override
    public String getVersion() {
        return "beta";
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
