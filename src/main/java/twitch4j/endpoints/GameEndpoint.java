package twitch4j.endpoints;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import twitch4j.TwitchClient;
import twitch4j.model.TopGame;
import twitch4j.model.TopGameList;
import twitch4j.util.rest.QueryRequestInterceptor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class GameEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 *
	 * @param client The Twitch Client.
	 */
	public GameEndpoint(TwitchClient client) {
		super(client, client.getRestClient().getRestTemplate());
	}

	/**
	 * Endpoint: Get Top Games
	 * Get games by number of current viewers on Twitch.
	 * Requires Scope: none
	 *
	 * @return todo
	 */
	public List<TopGame> getTopGames(@Nullable Integer limit, @Nullable Integer offset) {
		// REST Request
		try {
			RestTemplate restTemplate = this.restTemplate;

			if (limit != null) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
			}
			if (offset != null) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", Integer.toString((offset < 0) ? 0 : offset)));
			}

			TopGameList responseObject = restTemplate.getForObject("/games/top", TopGameList.class);

			return responseObject.getTop();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
	}
}
