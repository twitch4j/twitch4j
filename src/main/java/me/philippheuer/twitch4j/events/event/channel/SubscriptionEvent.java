package me.philippheuer.twitch4j.events.event.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.events.event.AbstractChannelEvent;
import twitch4j.api.kraken.json.Channel;
import twitch4j.api.kraken.json.Subscription;
import twitch4j.api.kraken.json.User;

/**
 * This event gets called when a user gets a new subscriber or a user resubscribes.
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
public class SubscriptionEvent extends AbstractChannelEvent {

	/**
	 * Event Target User
	 */
	private User user;

	/**
	 * The Subscription
	 */
	private Subscription subscription;

	/**
	 * Event Constructor
	 *
	 * @param channel      The channel that this event originates from.
	 * @param subscription The subscription, containing all relevant information.
	 */
	public SubscriptionEvent(Channel channel, Subscription subscription) {
		super(channel);
		this.user = subscription.getUser();
		this.subscription = subscription;
	}

}
