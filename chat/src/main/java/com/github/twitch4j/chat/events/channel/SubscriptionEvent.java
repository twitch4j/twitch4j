package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.chat.flag.AutoModFlag;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import java.util.List;
import java.util.Optional;

/**
 * This event gets called when a user gets a new subscriber or a user resubscribes.
 * <p>
 * This event will be called simultaneously with the chat announcement,
 * not when the user presses his subscription button.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class SubscriptionEvent extends AbstractChannelEvent {

    /**
     * Event Target User
     */
    EventUser user;

    /**
     * Subscription Plan
     */
    String subscriptionPlan;

    /**
     * Subscription Plan, in enum form
     */
    @Getter(lazy = true)
    SubscriptionPlan subPlan = SubscriptionPlan.fromString(subscriptionPlan);

    /**
     * Subscription Message
     */
    Optional<String> message;

    /**
     * Cumulative months subscribed
     */
    Integer months;

    /**
     * Was this sub gifted?
     */
    Boolean gifted;

    /**
     * User that gifted the sub
     */
    EventUser giftedBy;

    /**
     * Consecutive months subscribed
     */
    Integer subStreak;

    /**
     * The number of months gifted as part of a single, multi-month gift
     */
    Integer giftMonths;

    /**
     * The regions of {@link #getMessage()} that were flagged by AutoMod (Unofficial)
     */
    @Unofficial
    List<AutoModFlag> flags;

    /**
     * Event Constructor
     *
     * @param channel    ChatChannel the user subscribed to
     * @param user       User that subscribed
     * @param subPlan    Sub Plan
     * @param message    Sub Message
     * @param months     Cumulative number of months user has been subscribed (not consecutive)
     * @param gifted     Is gifted?
     * @param giftedBy   User that gifted the sub
     * @param subStreak  Consecutive number of months user has been subscribed (not cumulative); 0 if no streak or user chooses not to share their streak
     * @param giftMonths The number of months gifted as part of a single, multi-month gift
     * @param flags      The regions of the message that were flagged by AutoMod.
     */
    public SubscriptionEvent(EventChannel channel, EventUser user, String subPlan, Optional<String> message, Integer months, Boolean gifted, EventUser giftedBy, Integer subStreak, Integer giftMonths, List<AutoModFlag> flags) {
        super(channel);
        this.user = user;
        this.subscriptionPlan = subPlan;
        this.message = message;
        this.months = months;
        this.gifted = gifted;
        this.giftedBy = giftedBy;
        this.subStreak = subStreak;
        this.giftMonths = giftMonths;
        this.flags = flags;
    }

    /**
     * Gets the Subscription Plan
     *
     * @return SubscriptionPlan
     * @deprecated will be removed in favor of .getSubPlan()
     */
    @Deprecated
    public com.github.twitch4j.chat.enums.SubscriptionPlan getSubscriptionPlanName() {
        return com.github.twitch4j.chat.enums.SubscriptionPlan.fromString(this.subscriptionPlan);
    }

}
