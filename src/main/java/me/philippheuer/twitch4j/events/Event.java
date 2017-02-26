package me.philippheuer.twitch4j.events;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;

/**
 * Used to represent an event.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@Setter
public abstract class Event {

	/**
	 * Holds the TwitchClient Instance this event belongs to.
	 */
	protected TwitchClient client;

}
