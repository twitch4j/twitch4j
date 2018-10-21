package twitch4j.chat.events.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import twitch4j.chat.domain.Channel;
import twitch4j.chat.domain.User;
import twitch4j.chat.events.AbstractChannelEvent;

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
	private User user;

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
	 * @param subscriptionPlan The subscription plan
	 */
	public GiftSubscriptionsEvent(Channel channel, User user, String subscriptionPlan, Integer count, Integer totalCount) {
		super(channel);
		this.user = user;
		this.subscriptionPlan = subscriptionPlan;
		this.count = count;
		this.totalCount = totalCount;
	}

}
