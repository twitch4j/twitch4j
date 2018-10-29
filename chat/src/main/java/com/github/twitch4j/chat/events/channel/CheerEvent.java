package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import com.github.twitch4j.chat.domain.ChatChannel;
import com.github.twitch4j.chat.domain.ChatUser;

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
	private ChatUser user;

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
	public CheerEvent(ChatChannel channel, ChatUser user, String message, Integer bits) {
		super(channel);
		this.user = user;
		this.message = message;
		this.bits = bits;
	}
}
