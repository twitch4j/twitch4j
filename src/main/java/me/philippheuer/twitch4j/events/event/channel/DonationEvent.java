package me.philippheuer.twitch4j.events.event.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.events.event.AbstractChannelEvent;
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
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class DonationEvent extends AbstractChannelEvent {

	/**
	 * User
	 */
	private User user;

	/**
	 * Donation Source
	 */
	private String source;

	/**
	 * Donation Currency
	 */
	private Currency currency;

	/**
	 * Donation Amount
	 */
	private Double amount;

	/**
	 * Donation Message
	 */
	private String message;

	/**
	 * Event Constructor
	 *
	 * @param channel  The channel that this event originates from.
	 * @param user     The user who triggered the event.
	 * @param source   The source, where information was received from.
	 * @param currency The currency, that money was donated in.
	 * @param amount   The donated amount.
	 * @param message  The plain text of the message.
	 */
	public DonationEvent(Channel channel, User user, String source, Currency currency, Double amount, String message) {
		super(channel);
		this.user = user;
		this.source = source;
		this.currency = currency;
		this.amount = amount;
		this.message = message;
	}
}
