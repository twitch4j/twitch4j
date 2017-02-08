package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

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
	 * Constructor
	 *
	 * @param channel  Channel
	 * @param user     Target User
	 * @param duration Timeout Duration in Minutes.
	 * @param reason   Reason for Timeout.
	 */
	public UserTimeout(Channel channel, User user, Integer duration, String reason) {
		super();
		this.channel = channel;
		this.user = user;
		this.duration = duration;
		this.reason = reason;
	}
}
