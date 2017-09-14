package me.philippheuer.twitch4j.events;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;

import java.util.Calendar;

/**
 * Used to represent an event.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
public abstract class Event {

	/**
	 * Holds the TwitchClient Instance this event belongs to.
	 */
	@Setter(AccessLevel.PUBLIC)
	private TwitchClient client;

	/**
	 * Created At
	 */
	private final Calendar createdAt;

	/**
	 * Constructor
	 */
	public Event() {
		this.createdAt = Calendar.getInstance();
	}
}
