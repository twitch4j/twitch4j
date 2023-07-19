package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;

/**
 * This event gets called when the user starts hosting someone.
 *
 * @deprecated Twitch is removing host mode on October 3, 2022
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
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
