package twitch4j.chat.events.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.Channel;
import twitch4j.chat.domain.User;
import twitch4j.chat.events.AbstractChannelEvent;

import java.util.Currency;

/**
 * This event gets called when a user receives a donation from any source.
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
