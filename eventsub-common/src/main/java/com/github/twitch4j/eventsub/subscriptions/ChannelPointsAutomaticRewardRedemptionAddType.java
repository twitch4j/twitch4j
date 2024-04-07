package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition;
import com.github.twitch4j.eventsub.events.AutomaticRewardRedemptionAddEvent;

/**
 * This subscription type sends a notification when a viewer has redeemed an automatic channel points reward on the specified channel.
 * <p>
 * Must have channel:read:redemptions or channel:manage:redemptions scope.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_REDEMPTIONS_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_REDEMPTIONS_MANAGE
 */
public class ChannelPointsAutomaticRewardRedemptionAddType implements SubscriptionType<ChannelEventSubCondition, ChannelEventSubCondition.ChannelEventSubConditionBuilder<?, ?>, AutomaticRewardRedemptionAddEvent> {
    @Override
    public String getName() {
        return "channel.channel_points_automatic_reward_redemption.add";
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
    public Class<AutomaticRewardRedemptionAddEvent> getEventClass() {
        return AutomaticRewardRedemptionAddEvent.class;
    }
}
