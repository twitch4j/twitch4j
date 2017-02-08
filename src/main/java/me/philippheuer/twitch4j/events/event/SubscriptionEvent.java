package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.Subscription;
import me.philippheuer.twitch4j.model.User;

@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class SubscriptionEvent extends Event {

	/**
	 * Event Channel
	 */
	private final Channel channel;

	/**
	 * Event Target User
	 */
	private final User user;

	/**
	 * The Subscription
	 */
	private final Subscription subscription;

	/**
	 * Constructor
	 *
	 * @param channel      Channel
	 * @param subscription Subscription
	 */
	public SubscriptionEvent(Channel channel, Subscription subscription) {
		this.channel = channel;
		this.user = subscription.getUser();
		this.subscription = subscription;
	}

}
