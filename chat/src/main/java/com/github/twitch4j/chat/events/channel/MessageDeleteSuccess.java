package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

/**
 * Successfully deleted a message
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class MessageDeleteSuccess extends AbstractChannelEvent {

	/**
	 * Event Constructor
	 *
	 * @param channel       The channel that this event originates from.
	 */
	public MessageDeleteSuccess(EventChannel channel) {
		super(channel);
	}

}
