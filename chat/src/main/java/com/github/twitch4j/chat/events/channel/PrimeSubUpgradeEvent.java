package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Called when a user upgrades from a prime sub to a tiered subscription.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Unofficial
public class PrimeSubUpgradeEvent extends AbstractChannelEvent {
    /**
     * The user that is upgrading their subscription.
     */
    EventUser user;

    /**
     * The new subscription plan of the user.
     */
    SubscriptionPlan subscriptionPlan;

    /**
     * Constructor
     *
     * @param channel          the channel where the event took place
     * @param user             the user that is upgrading their subscription
     * @param subscriptionPlan the new subscription plan of the user
     */
    public PrimeSubUpgradeEvent(EventChannel channel, EventUser user, SubscriptionPlan subscriptionPlan) {
        super(channel);
        this.user = user;
        this.subscriptionPlan = subscriptionPlan;
    }
}
