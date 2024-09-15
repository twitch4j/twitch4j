package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import static com.github.twitch4j.common.util.TwitchUtils.ANONYMOUS_GIFTER;

/**
 * This event occurs when a recipient of a multi-month gift sub
 * enters a new month of the subscription, and shares a
 * re-subscription message, despite no additional monetary
 * transaction accompanying their message.
 * <p>
 * Webchat shows these events with this accompanying text:
 * {@code N month Gift Subscription courtesy of GIFTER_DISPLAY_NAME has entered month M.}
 * <p>
 * The parameters specific to this event are not officially documented.
 */
@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Unofficial
public class GiftedMultiMonthSubCourtesyEvent extends AbstractChannelEvent implements MirrorableEvent {

    /**
     * Raw message event.
     */
    @ToString.Exclude
    IRCMessageEvent messageEvent;

    /**
     * The gift recipient that is sending this resub message.
     */
    EventUser user;

    /**
     * The user-specified message attached to this notification.
     */
    String message;

    /**
     * The tier of the multi-month gifted subscription.
     */
    SubscriptionPlan subscriptionPlan;

    /**
     * Whether the original multi-month gift was done by an anonymous user.
     */
    boolean wasAnonymous;

    /**
     * The user that originally gifted the multi-month gift subscription.
     */
    EventUser gifter;

    /**
     * The number of months that were originally gifted as part of the multi-month subscription.
     */
    Integer giftMonths;

    /**
     * The gift month that is being redeemed.
     */
    Integer redeemedMonth;

    /**
     * The cumulative number of months the user has been subscribed to this channel.
     */
    Integer cumulativeMonths;

    /**
     * Event Constructor
     *
     * @param event the raw message event
     */
    public GiftedMultiMonthSubCourtesyEvent(IRCMessageEvent event) {
        super(event.getChannel());
        this.messageEvent = event;
        this.user = event.getUser();
        this.message = event.getMessage().orElse(null);
        this.subscriptionPlan = SubscriptionPlan.fromString(event.getTagValue("msg-param-sub-plan").orElse("1000"));
        this.wasAnonymous = StringUtils.equalsIgnoreCase("true", event.getRawTag("msg-param-anon-gift"));
        this.gifter = wasAnonymous ? ANONYMOUS_GIFTER : new EventUser(
            event.getRawTagString("msg-param-gifter-id"),
            event.getTagValue("msg-param-gifter-login").orElse(event.getTagValue("msg-param-gifter-name").orElse(null))
        );
        this.giftMonths = Integer.parseInt(event.getTagValue("msg-param-gift-months").orElse("0"));
        this.redeemedMonth = Integer.parseInt(event.getTagValue("msg-param-gift-month-being-redeemed").orElse("0"));
        this.cumulativeMonths = Integer.parseInt(event.getTagValue("msg-param-cumulative-months").orElse("0"));
    }

}
