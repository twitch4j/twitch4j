package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * This event gets called when the user stops hosting someone.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class HostOffEvent extends TwitchEvent {

	/**
	 * Event ChatChannel
	 */
	private EventChannel channel;

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 */
	public HostOffEvent(EventChannel channel) {
		this.channel = channel;
	}

}
