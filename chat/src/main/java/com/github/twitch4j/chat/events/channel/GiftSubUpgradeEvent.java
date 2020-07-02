package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Called when a user upgrades to a paid subscription from previously being gifted a subscription.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GiftSubUpgradeEvent extends AbstractChannelEvent {
    /**
     * The user that is upgrading their subscription.
     */
    EventUser upgradingUser;

    /**
     * The subscriptions promo, if any, that is ongoing; e.g. Subtember 2018.
     */
    String promoName;

    /**
     * The number of gifts the gifter has given during the promo indicated by {@link #getPromoName()}.
     */
    Integer promoGiftTotal;

    /**
     * The login of the user who gifted the subscription, if applicable.
     */
    String gifterLogin;

    /**
     * The display name of the user who gifted the subscription, if applicable.
     */
    String gifterName;

    /**
     * Constructor
     *
     * @param channel        the channel where the event took place
     * @param upgradingUser  the user that is upgrading their subscription
     * @param promoName      the ongoing subscriptions promo, if applicable
     * @param promoGiftTotal the number of gifts the gifter has given during the promo
     * @param gifterLogin    the login of the user who gifted the subscription, if applicable
     * @param gifterName     the display name of the user who gifted the subscription, if applicable
     */
    public GiftSubUpgradeEvent(EventChannel channel, EventUser upgradingUser, String promoName, Integer promoGiftTotal, String gifterLogin, String gifterName) {
        super(channel);
        this.upgradingUser = upgradingUser;
        this.promoName = promoName;
        this.promoGiftTotal = promoGiftTotal;
        this.gifterLogin = gifterLogin;
        this.gifterName = gifterName;
    }
}
