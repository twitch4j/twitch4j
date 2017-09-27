package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.RestError;
import me.philippheuer.twitch4j.model.Team;
import me.philippheuer.twitch4j.model.TeamList;
import me.philippheuer.util.rest.QueryRequestInterceptor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class TeamEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 *
	 * @param client todo
	 */
	public TeamEndpoint(TwitchClient client) {
		super(client);
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
	public List<Team> getTeams(Optional<Long> limit, Optional<Long> offset) {
		// Endpoint
		String requestUrl = String.format("%s/teams", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", offset.orElse(0l).toString()));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			TeamList responseObject = restTemplate.getForObject(requestUrl, TeamList.class);

			return responseObject.getTeams();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Endpoint: Get Team
	 * Gets a specified team object.
	 * Requires Scope: none
	 *
	 * @param teamName Name of the Team.
	 * @return Optional of type Team, is only present - if the team exists.
	 */
	public Optional<Team> getTeam(String teamName) {
		// Endpoint
		String requestUrl = String.format("%s/teams/%s", Endpoints.API.getURL(), teamName);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			Team responseObject = restTemplate.getForObject(requestUrl, Team.class);

			return Optional.ofNullable(responseObject);
		} catch (RestException restException) {
			RestError restError = restException.getRestError();
			Logger.error(this, "RestException: " + restError.toString());

			// Handle: Team does not exist
			if (restError.getStatus().equals(404) && restError.getMessage().equals("Team does not exist")) {
				return Optional.empty();
			}
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		throw new RuntimeException("Unhandled Exception!");
	}

}
