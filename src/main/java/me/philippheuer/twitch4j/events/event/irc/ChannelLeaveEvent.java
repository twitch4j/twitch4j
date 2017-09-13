package me.philippheuer.twitch4j.events.event.irc;

import lombok.*;
import me.philippheuer.twitch4j.events.event.AbstractChannelEvent;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

/**
 * This event gets called when a client leaves the channel.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class ChannelLeaveEvent extends AbstractChannelEvent {

	/**
	 * User
	 */
	private User user;

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 * @param user        The user triggering the event.
	 */
	public ChannelLeaveEvent(Channel channel, User user) {
		super(channel);
		setUser(user);
	}
}
