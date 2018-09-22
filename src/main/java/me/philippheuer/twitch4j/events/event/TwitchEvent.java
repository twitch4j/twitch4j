package me.philippheuer.twitch4j.events.event;

import com.github.philippheuer.events4j.domain.Event;
import lombok.Data;
import me.philippheuer.twitch4j.TwitchClient;

@Data
public abstract class TwitchEvent extends Event {

	/**
	 * Constructor
	 */
	public TwitchEvent() {
		super();
	}

	/**
	 * Gets a instance of the TwitchClient
	 *
	 * @return TwitchClient
	 */
	public TwitchClient getClient() {
		return getServiceMediator().getService(TwitchClient.class, "twitch4j");
	}

}
