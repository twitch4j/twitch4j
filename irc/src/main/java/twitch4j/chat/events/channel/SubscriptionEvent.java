package twitch4j.chat.events.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.Channel;
import twitch4j.chat.domain.User;
import twitch4j.chat.events.AbstractChannelEvent;

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
	private User user;

	/**
	 * Subscription Plan
	 */
    private String subscriptionPlan;

    /**
     * Subscription Plan Name
     */
    private String subscriptionPlanName;

	/**
	 * Subscription Message
	 */
	private Optional<String> message;

	/**
	 * X Months subscription streak
	 */
	private Integer months;

	/**
	 * Was this sub gifted?
	 */
	private Boolean gifted;

	/**
	 * User that gifted the sub
	 */
	private User giftedBy;

    /**
     * Event Constructor
     *
     * @param channel Channel the user subscribed to
     * @param user User that subscribed
     * @param subPlan Sub Plan
     * @param subPlanName Sub Plan Name
     * @param message Sub Message
     * @param months Months
     * @param gifted Is gifted?
     * @param giftedBy User that gifted the sub
     */
	public SubscriptionEvent(Channel channel, User user, String subPlan, String subPlanName, Optional<String> message, Integer months, Boolean gifted, User giftedBy) {
		super(channel);
		this.user = user;
		this.subscriptionPlan = subPlan;
		this.subscriptionPlanName = subPlanName;
		this.message = message;
		this.months = months;
		this.gifted = gifted;
		this.giftedBy = giftedBy;
	}

}
