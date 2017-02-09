package me.philippheuer.twitch4j.endpoints;

import java.util.List;
import java.util.Optional;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.model.*;
import org.springframework.web.client.RestTemplate;

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
	 * Get games by number of current viewers on Twitch.
	 * Requires Scope: none
	 */
	public List<TopGame> getTopGames() {
		// Endpoint
		String requestUrl = String.format("%s/games/top", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			TopGameList responseObject = restTemplate.getForObject(requestUrl, TopGameList.class);

			return responseObject.getTop();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
