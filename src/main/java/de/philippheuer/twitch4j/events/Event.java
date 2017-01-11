package de.philippheuer.twitch4j.events;

import de.philippheuer.twitch4j.TwitchClient;

import lombok.*;

@Getter
@Setter
public abstract class Event {
	
	/**
	 * Holds the TwitchClient Instance this event belongs to.
	 */
	protected TwitchClient client;
}
