package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.model.TopGame;
import me.philippheuer.twitch4j.model.TopGameList;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Getter
@Setter
public class GameEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 *
	 * @param client todo
	 */
	public GameEndpoint(TwitchClient client) {
		super(client);
	}

	/**
	 * Endpoint: Get Top Games
	 * Get games by number of current viewers on Twitch.
	 * Requires Scope: none
	 *
	 * @return todo
	 */
	public List<TopGame> getTopGames() {
		// Endpoint
		String requestUrl = String.format("%s/games/top", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			TopGameList responseObject = restTemplate.getForObject(requestUrl, TopGameList.class);

			return responseObject.getTop();
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}
}
