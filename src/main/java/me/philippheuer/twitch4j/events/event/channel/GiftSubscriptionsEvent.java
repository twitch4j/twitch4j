package me.philippheuer.twitch4j.events.event.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import me.philippheuer.twitch4j.enums.SubscriptionPlan;
import me.philippheuer.twitch4j.events.event.AbstractChannelEvent;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

/**
 * This event gets called when a user gifts x subscriptions to *random* users in chat.
 * <p>
 * This event will be called simultaneously with the chat announcement,
 * not when the user presses his subscription button.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class GiftSubscriptionsEvent extends AbstractChannelEvent {

	/**
	 * Event Target User
	 */
	private User user;

	/**
	 * The Subscription
	 */
	private SubscriptionPlan subscriptionPlan;

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
	 * @param subscriptionPlan The subscription plan
	 */
	public GiftSubscriptionsEvent(Channel channel, User user, String subscriptionPlan, Integer count, Integer totalCount) {
		super(channel);
		this.user = user;
		this.subscriptionPlan = SubscriptionPlan.fromString(subscriptionPlan);
		this.count = count;
		this.totalCount = totalCount;
	}

}
