package me.philippheuer.twitch4j.events.event.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.events.event.AbstractChannelEvent;
import me.philippheuer.twitch4j.model.Channel;

/**
 * This event gets called when the user starts hosting someone.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class HostOnEvent extends AbstractChannelEvent {

	/**
	 * Event Target Channel
	 */
	private Channel targetChannel;

	/**
	 * Event Constructor
	 *
	 * @param channel       The channel that this event originates from.
	 * @param targetChannel The channel that was hosted.
	 */
	public HostOnEvent(Channel channel, Channel targetChannel) {
		super(channel);
		this.targetChannel = targetChannel;
	}

}
