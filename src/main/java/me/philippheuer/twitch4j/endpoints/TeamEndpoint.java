package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class TeamEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 */
	public TeamEndpoint(TwitchClient client) {
		super(client);
	}

	/**
	 * Endpoint: Get All Teams
	 * Gets all active teams.
	 * Requires Scope: none
	 * @param limit
	 * @param offset
	 */
	public List<Team> getTeams() {
		// Endpoint
		String requestUrl = String.format("%s/teams", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			TeamList responseObject = restTemplate.getForObject(requestUrl, TeamList.class);

			return responseObject.getTeams();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Endpoint: Get Team
	 * Gets a specified team object.
	 * Requires Scope: none
	 * @return Optional<Team>, is only present - if the team exists.
	 */
	public Optional<Team> getTeam(String teamName) {
		// Endpoint
		String requestUrl = String.format("%s/teams/%s", getTwitchClient().getTwitchEndpoint(), teamName);
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
			if(restError.getStatus().equals(404) && restError.getMessage().equals("Team does not exist")) {
				return Optional.empty();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		throw new RuntimeException("Unhandled Exception!");
	}
}
