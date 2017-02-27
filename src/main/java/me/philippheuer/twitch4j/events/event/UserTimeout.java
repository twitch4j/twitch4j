package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

/**
 * This event gets called when a user get a timeout.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserTimeout extends Event {

	/**
	 * Event Channel
	 */
	private final Channel channel;

	/**
	 * Event Target User
	 */
	private final User user;

	/**
	 * Duration in Minutes
	 */
	private final Integer duration;

	/**
	 * Reason for Punishment
	 */
	private final String reason;

	/**
	 * Event Constructor
	 *
	 * @param channel  The channel that this event originates from.
	 * @param user     The user who triggered the event.
	 * @param duration Timeout Duration in Minutes.
	 * @param reason   Reason for Ban.
	 */
	public UserTimeout(Channel channel, User user, Integer duration, String reason) {
		super();
		this.channel = channel;
		this.user = user;
		this.duration = duration;
		this.reason = reason;
	}
}
