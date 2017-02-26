package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

/**
 * This event gets called when a user gets a new follower.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class FollowEvent extends Event {

	/**
	 * Event Channel
	 */
	private final Channel channel;

	/**
	 * User
	 */
	private final User user;

	/**
	 * Constructor
	 */
	public FollowEvent(Channel channel, User user) {
		this.channel = channel;
		this.user = user;
	}
}
