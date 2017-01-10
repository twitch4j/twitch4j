package de.philippheuer.twitch4j.endpoints;

import java.util.List;
import java.util.Optional;

import de.philippheuer.twitch4j.TwitchClient;
import de.philippheuer.twitch4j.model.*;

import lombok.*;

@Getter
@Setter
public class GameEndpoint extends AbstractTwitchEndpoint {
	
	/**
	 * Get User by UserId
	 */
	public GameEndpoint(TwitchClient api) {
		super(api);
	}
	
	/**
	 * Endpoint: Get Top Games
	 *  Get games by number of current viewers on Twitch.
	 * Requires Scope: none
	 */
	public Optional<List<TopGame>> getTopGames() {
		// REST Request
		try {
			String requestUrl = String.format("%s/games/top", getApi().getTwitchEndpoint());
			TopGameList responseObject = getRestTemplate().getForObject(requestUrl, TopGameList.class);
			
			return Optional.ofNullable(responseObject.getTop());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
}
