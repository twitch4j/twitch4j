package twitch4j.endpoints;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;
import twitch4j.TwitchClient;
import twitch4j.model.Team;
import twitch4j.model.TeamList;
import twitch4j.util.rest.QueryRequestInterceptor;

@Slf4j
public class TeamEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 *
	 * @param client The Twitch Client.
	 */
	public TeamEndpoint(TwitchClient client) {
		super(client, client.getRestClient().getRestTemplate());
	}

	/**
	 * Endpoint: Get All Teams
	 * Gets all active teams.
	 * Requires Scope: none
	 *
	 * @param limit  Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @param offset Object offset for pagination. Default is 0.
	 * @return todo
	 */
	public List<Team> getTeams(@Nullable Integer limit, @Nullable Integer offset) {
		// Endpoint
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
		}
		if (offset != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", Integer.toString((offset < 0) ? 0 : offset)));
		}

		// REST Request
		try {
			return restTemplate.getForObject("/teams", TeamList.class).getTeams();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
	}

	/**
	 * Endpoint: Get Team
	 * Gets a specified team object.
	 * Requires Scope: none
	 *
	 * @param teamName Name of the Team.
	 * @return Optional of type Team, is only present - if the team exists.
	 */
	public Team getTeam(String teamName) {
		// Endpoint
		String requestUrl = String.format("/teams/%s", teamName);

		// REST Request
		try {
			return restTemplate.getForObject(requestUrl, Team.class);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return null;
		}
	}

}
