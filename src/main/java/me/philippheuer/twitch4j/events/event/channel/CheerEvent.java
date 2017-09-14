package me.philippheuer.twitch4j.events.event.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.events.event.AbstractChannelEvent;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

/**
 * This event gets called when a user receives bits.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class CheerEvent extends AbstractChannelEvent {

	/**
	 * Event Target User
	 */
	private User user;

	/**
	 * Message
	 */
	private String message;

	/**
	 * Amount of Bits
	 */
	private Integer bits;

	/**
	 * Event Constructor
	 *
	 * @param channel The channel that this event originates from.
	 * @param user The donating user.
	 * @param message The donation message.
	 * @param bits The amount of bits.
	 */
	public CheerEvent(Channel channel, User user, String message, Integer bits) {
		super(channel);
		this.user = user;
		this.message = message;
		this.bits = bits;
	}
}
