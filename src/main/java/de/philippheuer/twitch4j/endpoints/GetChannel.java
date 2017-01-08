package de.philippheuer.twitch4j.endpoints;

import de.philippheuer.twitch4j.Twitch4J;

import lombok.*;


/**
 * Gets the Channel Info of the currently signed in user
 * @requires_scope channel_read
 */
public class GetChannel extends AbstractTwitchEndpoint {
	
	/**
	 * Get
	 */
	public GetChannel(Twitch4J api) {
		super(api);
		
		
	}
	
	
}
