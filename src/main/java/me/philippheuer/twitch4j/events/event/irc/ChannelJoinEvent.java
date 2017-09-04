package me.philippheuer.twitch4j.events.event.irc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.event.AbstractChannelEvent;
import me.philippheuer.twitch4j.model.Channel;

/**
 * This event gets called when a message is received in a channel.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ChannelJoinEvent extends AbstractChannelEvent {

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 */
	public ChannelJoinEvent(Channel channel) {
		super(channel);
	}
}
