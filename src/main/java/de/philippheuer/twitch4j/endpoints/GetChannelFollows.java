package de.philippheuer.twitch4j.endpoints;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import de.philippheuer.twitch4j.Twitch4J;
import de.philippheuer.twitch4j.model.*;

import lombok.*;


/**
 * 
 * @requires_scope none
 */
@Data
public class GetChannelFollows extends AbstractTwitchEndpoint {
	
	/**
	 * Get
	 */
	public GetChannelFollows(Twitch4J api, String channelId) {
		super(api);
		
		
	}
	
	
}
