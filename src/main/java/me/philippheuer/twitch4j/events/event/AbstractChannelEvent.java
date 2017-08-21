package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;

/**
 * This event is a base for events that originate from a channel.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class AbstractChannelEvent extends Event {

	/**
	 * Event Channel
	 */
	private final Channel channel;

	/**
	 * Event Constructor
	 *
	 * @param channel The channel that this event originates from.
	 */
	public AbstractChannelEvent(Channel channel) {
		this.channel = channel;
	}

	/**
	 * Method to send messages to the channel the event originates from.
	 *
	 * @param message  The plain text of the message.
	 */
	public void sendMessage(String message) {
		getClient().getMessageInterface().sendMessage(channel.getName(), message);
	}

}
