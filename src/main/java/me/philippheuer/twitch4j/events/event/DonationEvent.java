package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

import java.util.Currency;

/**
 * This event gets called when a user receives a donation from any source.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class DonationEvent extends Event {

	/**
	 * Event Channel
	 */
	private final Channel channel;

	/**
	 * User
	 */
	private final User user;

	/**
	 * Donation Source
	 */
	private final String source;

	/**
	 * Donation Currency
	 */
	private final Currency currency;

	/**
	 * Donation Amount
	 */
	private Double amount;

	/**
	 * Donation Message
	 */
	private String message;

	/**
	 * Constructor
	 */
	public DonationEvent(Channel channel, User user, String source, Currency currency, Double amount, String message) {
		this.channel = channel;
		this.user = user;
		this.source = source;
		this.currency = currency;
		this.amount = amount;
		this.message = message;
	}
}
