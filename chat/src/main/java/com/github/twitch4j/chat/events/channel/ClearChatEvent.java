package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ClearChatEvent extends AbstractChannelEvent {

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 */
	public ClearChatEvent(EventChannel channel) {
		super(channel);
	}
}
