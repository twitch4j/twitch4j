package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.domain.ChatChannel;
import com.github.twitch4j.chat.domain.ChatUser;
import com.github.twitch4j.chat.events.AbstractChannelEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

/**
 * This event gets called when a user gifts x subscriptions to *random* users in chat.
 * <p>
 * This event will be called simultaneously with the chat announcement,
 * not when the user presses his subscription button.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class GiftSubscriptionsEvent extends AbstractChannelEvent {

    /**
     * Event Target User
     */
    private ChatUser user;

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
     * @param channel          The channel that this event originates from.
     * @param user             The user that gifted the subscriptions
     * @param subscriptionPlan The subscription plan
     * @param count            The total amount of subs gifted
     */
    public GiftSubscriptionsEvent(ChatChannel channel, ChatUser user, String subscriptionPlan, Integer count, Integer totalCount) {
        super(channel);
        this.user = user;
        this.subscriptionPlan = subscriptionPlan;
        this.count = count;
        this.totalCount = totalCount;
    }

}
