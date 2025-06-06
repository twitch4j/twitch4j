package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;

/**
 * This event gets called when a user gifts x subscriptions to *random* users in chat.
 * <p>
 * This event will be called simultaneously with the chat announcement,
 * not necessarily when the user presses the subscription button.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GiftSubscriptionsEvent extends AbstractChannelEvent implements MirrorableEvent {

    /**
     * Raw Message Event
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    IRCMessageEvent messageEvent;

    /**
     * Event Target User
     */
    EventUser user;

    /**
     * The Subscription
     */
    String subscriptionPlan;

    /**
     * X subscriptions gifted
     */
    int count;

    /**
     * X subscriptions gifted totally
     */
    int totalCount;

    /**
     * A unique identifier that links the community gift event to each individual recipient gifted event.
     *
     * @apiNote While this field is undocumented in irc, it is equivalent to the documented eventsub field {@code community_gift_id}.
     */
    @Unofficial
    String giftId;

    /**
     * Event Constructor
     *
     * @param event            The raw message event
     * @param channel          The channel that this event originates from.
     * @param user             The user that gifted the subscriptions
     * @param subscriptionPlan The subscription plan
     * @param count            The total amount of subs gifted
     * @param totalCount       The amount the user gifted in total (all time)
     */
    @ApiStatus.Internal
    public GiftSubscriptionsEvent(IRCMessageEvent event, EventChannel channel, EventUser user, String subscriptionPlan, int count, int totalCount, String giftId) {
        super(channel);
        this.messageEvent = event;
        this.user = user;
        this.subscriptionPlan = subscriptionPlan;
        this.count = count;
        this.totalCount = totalCount;
        this.giftId = giftId;
    }

    /**
     * @return the raw subscription plan
     * @deprecated in favor of {@link #getTier()}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public String getSubscriptionPlan() {
        return subscriptionPlan;
    }

    /**
     * @return the tier of the subscription that was gifted
     */
    public SubscriptionPlan getTier() {
        return SubscriptionPlan.fromString(this.subscriptionPlan);
    }

}
