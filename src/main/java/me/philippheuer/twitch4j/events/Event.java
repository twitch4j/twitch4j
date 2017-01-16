package me.philippheuer.twitch4j.events;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;

@Getter
@Setter
public abstract class Event {

	/**
	 * Holds the TwitchClient Instance this event belongs to.
	 */
	protected TwitchClient client;

}
