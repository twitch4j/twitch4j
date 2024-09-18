package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
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
    private IRCMessageEvent messageEvent;

    /**
     * Event Target User
     */
    private EventUser user;

    /**
     * The Subscription
     */
    private String subscriptionPlan;

    /**
     * X subscriptions gifted
     */
    private Integer count;

    /**
     * X subscriptions gifted totally
     */
    private Integer totalCount;

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
    public GiftSubscriptionsEvent(IRCMessageEvent event, EventChannel channel, EventUser user, String subscriptionPlan, Integer count, Integer totalCount) {
        super(channel);
        this.messageEvent = event;
        this.user = user;
        this.subscriptionPlan = subscriptionPlan;
        this.count = count;
        this.totalCount = totalCount;
    }

}
