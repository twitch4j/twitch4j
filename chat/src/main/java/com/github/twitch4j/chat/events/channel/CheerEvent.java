package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * This event gets called when a user receives bits.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class CheerEvent extends AbstractChannelEvent {

	/**
	 * Event Target User
	 */
	private EventUser user;

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
	public CheerEvent(EventChannel channel, EventUser user, String message, Integer bits) {
		super(channel);
		this.user = user;
		this.message = message;
		this.bits = bits;
	}
}
