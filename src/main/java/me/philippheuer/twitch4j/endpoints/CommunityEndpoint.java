package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.helper.QueryRequestInterceptor;
import me.philippheuer.twitch4j.model.Community;
import me.philippheuer.twitch4j.model.CommunityCreate;
import me.philippheuer.twitch4j.model.CommunityList;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class CommunityEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 */
	public CommunityEndpoint(TwitchClient client) {
		super(client);
	}

	/**
	 * Endpoint: Get Community by Name
	 * Gets a specified community.
	 * Requires Scope: none
	 *
	 * @param name The name of the community is specified in a required query-string parameter. It must be 3-25 characters.
	 */
	public Community getCommunityByName(String name) {
		// Endpoint
		String requestUrl = String.format("%s/communities", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameter
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("name", name));

		// REST Request
		try {
			Community responseObject = restTemplate.getForObject(requestUrl, Community.class);

			return responseObject;
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Endpoint: Get Community by ID
	 * Gets a specified community.
	 * Requires Scope: none
	 *
	 * @param id The guid of the community. (e9f17055-810f-4736-ba40-fba4ac541caa)
	 */
	public Community getCommunityById(String id) {
		// Endpoint
		String requestUrl = String.format("%s/communities/%s", getTwitchClient().getTwitchEndpoint(), id);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			Community responseObject = restTemplate.getForObject(requestUrl, Community.class);

			return responseObject;
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Endpoint: Create Community
	 * Creates a community.
	 * Requires Scope: none
	 *
	 * @param credential  OAuth token for a Twitch user (that as 2fa enabled)
	 * @param name        Community name. 3-25 characters, which can be alphanumerics, dashes (-), periods (.), underscores (_), and tildes (~). Cannot contain spaces.
	 * @param summary     Short description of the community, shown in search results. Maximum: 160 characters.
	 * @param description Long description of the community, shown in the “about this community” box. Markdown syntax allowed. Maximum 1,572,864 characters (1.5 MB).
	 * @param rules       Rules displayed when viewing a community page or searching for a community from the broadcaster dashboard. Markdown syntax allowed. Maximum 1,572,864 characters (1.5 MB)
	 * @return ID (String) of the created community
	 */
	public String createCommunity(OAuthCredential credential, String name, String summary, String description, String rules) {
		// Endpoint
		String requestUrl = String.format("%s/communities", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Post Data
		MultiValueMap<String, Object> postBody = new LinkedMultiValueMap<String, Object>();
		postBody.add("name", name);
		postBody.add("summary", summary);
		postBody.add("description", description);
		postBody.add("rules", rules);

		// REST Request
		try {
			CommunityCreate responseObject = restTemplate.postForObject(requestUrl, postBody, CommunityCreate.class);

			return responseObject.getId();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Endpoint: Update Community
	 * Creates a community.
	 * Requires Scope: none
	 *
	 * @param credential  OAuth token for a Twitch user (that as 2fa enabled)
	 * @param id Id of the community, which will be updated.
	 * @param name        Community name. 3-25 characters, which can be alphanumerics, dashes (-), periods (.), underscores (_), and tildes (~). Cannot contain spaces.
	 * @param summary     Short description of the community, shown in search results. Maximum: 160 characters.
	 * @param description Long description of the community, shown in the “about this community” box. Markdown syntax allowed. Maximum 1,572,864 characters (1.5 MB).
	 * @param rules       Rules displayed when viewing a community page or searching for a community from the broadcaster dashboard. Markdown syntax allowed. Maximum 1,572,864 characters (1.5 MB)
	 * @param email Email address of the community owner.
	 * @return ID (String) of the created community
	 */
	public void updateCommunity(OAuthCredential credential, String id, Optional<String> name, Optional<String> summary, Optional<String> description, Optional<String> rules, Optional<String> email) {
		// Endpoint
		String requestUrl = String.format("%s/communities/%s", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Post Data
		MultiValueMap<String, Object> postBody = new LinkedMultiValueMap<String, Object>();
		postBody.add("summary", summary.orElse(""));
		postBody.add("description", description.orElse(""));
		postBody.add("rules", rules.orElse(""));
		postBody.add("email", email.orElse(""));

		// REST Request
		try {
			restTemplate.postForObject(requestUrl, postBody, CommunityCreate.class);
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return;
	}

	/**
	 * Endpoint: Get Top Communities
	 * Gets a specified community.
	 * Requires Scope: none
	 *
	 * @param limit     Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 */
	public List<Community> getTopCommunities(Optional<Integer> limit) {
		// Endpoint
		String requestUrl = String.format("%s/communities/top", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25).toString()));

		// REST Request
		try {
			CommunityList responseObject = restTemplate.getForObject(requestUrl, CommunityList.class);

			return responseObject.getCommunities();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	// TODO
	// Get Community Banned Users
	// Ban Community User
	// Un-Ban Community User
	// Create Community Avatar Image
	// Delete Community Avatar Image
	// Create Community Cover Image
	// Delete Community Cover Image
	// Get Community Moderators
	// Add Community Moderator
	// Delete Community Moderator
	// Get Community Permissions
	// Report Community Violation
	// Get Community Timed-Out Users
	// Add Community Timed-Out User
	// Delete Community Timed-Out User

	private Boolean validateCommunityName(String name) {
		Pattern pattern = Pattern.compile("[A-Za-z\\-\\.\\_\\~]{3,25}");

		return true;
	}
}
