package twitch4j.api.kraken.endpoints;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;
import twitch4j.api.kraken.model.TopGame;
import twitch4j.api.kraken.model.TopGameList;
import twitch4j.api.util.rest.QueryRequestInterceptor;

@Slf4j
public class GameEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 *
	 * @param restTemplate The Twitch Client.
	 */
	public GameEndpoint(RestTemplate restTemplate) {
		super(restTemplate);
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
