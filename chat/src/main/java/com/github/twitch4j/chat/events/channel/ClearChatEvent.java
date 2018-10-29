package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import com.github.twitch4j.chat.domain.ChatChannel;

@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ClearChatEvent extends AbstractChannelEvent {

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 */
	public ClearChatEvent(ChatChannel channel) {
		super(channel);
	}
}
