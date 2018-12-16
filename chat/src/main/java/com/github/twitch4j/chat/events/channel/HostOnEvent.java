package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * This event gets called when the user starts hosting someone.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class HostOnEvent extends AbstractChannelEvent {

	/**
	 * Event Target ChatChannel
	 */
	private EventChannel targetChannel;

	/**
	 * Event Constructor
	 *
	 * @param channel       The channel that this event originates from.
	 * @param targetChannel The channel that was hosted.
	 */
	public HostOnEvent(EventChannel channel, EventChannel targetChannel) {
		super(channel);
		this.targetChannel = targetChannel;
	}

}
