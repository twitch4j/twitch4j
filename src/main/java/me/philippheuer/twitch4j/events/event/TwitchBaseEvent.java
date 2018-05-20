package me.philippheuer.twitch4j.events.event;

import com.github.philippheuer.events4j.domain.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;

/**
 * This event is a base for all twitch events.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@EqualsAndHashCode(callSuper = false)
public class TwitchBaseEvent extends Event {

	/**
	 * Constructor
	 */
	public TwitchBaseEvent() {
		super();
	}

	/**
	 * Gets the twitch client using the service mediator
	 *
	 * @return The TwitchClient Instance.
	 */
	public TwitchClient getTwitchClient() {
		return getServiceMediator().getService(TwitchClient.class, "twitch4j");
	}
}
