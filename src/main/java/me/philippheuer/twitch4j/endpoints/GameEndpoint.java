package me.philippheuer.twitch4j.endpoints;

import java.util.List;
import java.util.Optional;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.model.*;

@Getter
@Setter
public class GameEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 */
	public GameEndpoint(TwitchClient client) {
		super(client);
	}

	/**
	 * Endpoint: Get Top Games
	 *  Get games by number of current viewers on Twitch.
	 * Requires Scope: none
	 */
	public Optional<List<TopGame>> getTopGames() {
		// REST Request
		try {
			String requestUrl = String.format("%s/games/top", getTwitchClient().getTwitchEndpoint());
			TopGameList responseObject = getTwitchClient().getRestClient().getRestTemplate().getForObject(requestUrl, TopGameList.class);

			return Optional.ofNullable(responseObject.getTop());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
}
