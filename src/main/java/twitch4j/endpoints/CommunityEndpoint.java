package twitch4j.endpoints;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import twitch4j.TwitchClient;
import twitch4j.auth.model.OAuthCredential;
import twitch4j.enums.Scope;
import twitch4j.exceptions.ChannelCredentialMissingException;
import twitch4j.exceptions.ScopeMissingException;
import twitch4j.model.BannedCommunityUsers;
import twitch4j.model.Community;
import twitch4j.model.CommunityList;
import twitch4j.util.rest.HeaderRequestInterceptor;
import twitch4j.util.rest.QueryRequestInterceptor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class CommunityEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 *
	 * @param client The Twitch Client.
	 */
	public CommunityEndpoint(TwitchClient client) {
		super(client, client.getRestClient().getRestTemplate());
	}

	/**
	 * Endpoint: Get Community by Name
	 * Gets a specified community.
	 * Requires Scope: none
	 *
	 * @param name The name of the community is specified in a required query-string parameter. It must be 3-25 characters.
	 * @return todo
	 */
	public Community getCommunityByName(String name) {

		// REST Request
		try {
			Objects.requireNonNull(name, "Provide community name!");
			Assert.isTrue(validateCommunityName(name), "Invalid community name!");

			// Endpoint
			String requestUrl = "/communities";
			RestTemplate restTemplate = this.restTemplate;

			// Parameter
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("name", name));
			return restTemplate.getForObject(requestUrl, Community.class);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Endpoint: Get Community by ID
	 * Gets a specified community.
	 * Requires Scope: none
	 *
	 * @param id The guid of the community. (e9f17055-810f-4736-ba40-fba4ac541caa)
	 * @return The community.
	 */
	public Community getCommunityById(String id) {
		try {
			Objects.requireNonNull(id, "Provide community ID!");
			// Endpoint
			String requestUrl = String.format("/communities/%s", id);

			// REST Request
			return restTemplate.getForObject(requestUrl, Community.class);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return null;
		}
	}

	/**
	 * Endpoint: Update Community
	 * Creates a community.
	 * Requires Scope: none
	 *
	 * @param credential  OAuth token for a Twitch user (that as 2fa enabled)
	 * @param id          Id of the community, which will be updated.
	 * @param summary     Short description of the community, shown in search results. Maximum: 160 characters.
	 * @param description Long description of the community, shown in the *about this community* box. Markdown syntax allowed. Maximum 1,572,864 characters (1.5 MB).
	 * @param rules       Rules displayed when viewing a community page or searching for a community from the broadcaster dashboard. Markdown syntax allowed. Maximum 1,572,864 characters (1.5 MB)
	 * @param email       Email address of the community owner.
	 */
	public Boolean updateCommunity(OAuthCredential credential, String id, @Nullable String summary, @Nullable String description, @Nullable String rules, @Nullable String email) {
		try {
			checkScopePermission(credential.getOAuthScopes(), Scope.COMMUNITIES_EDIT);

			// Endpoint
			String requestUrl = String.format("/communities/%s", id);
			RestTemplate restTemplate = this.restTemplate;

			// Post Data
			Map<String, Object> postBody = new LinkedHashMap<>();
			if (summary != null) {
				postBody.put("summary", summary);
			}
			if (description != null) {
				postBody.put("description", description);
			}
			if (rules != null) {
				postBody.put("rules", rules);
			}
			if (email != null) {
				postBody.put("email", email);
			}

			if (postBody.isEmpty()) {
				throw new NullPointerException("Must be contain some data!");
			}

			// Header Parameters
			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.getToken())));

			restTemplate.put(requestUrl, postBody);

			return true;
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.getUserId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return false;
		}
	}

	/**
	 * Endpoint: Get Top Communities
	 * Gets a specified community.
	 * Requires Scope: none
	 *
	 * @param limit  Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @param cursor Tells the server where to start fetching the next set of results in a multi-page response.
	 * @return The top communities.
	 */
	public CommunityList getTopCommunities(@Nullable Integer limit, @Nullable String cursor) {
		// Endpoint
		String requestUrl = "/communities/top";
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
		}
		if (cursor != null && cursor.equals("")) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("cursor", cursor));
		}

		// REST Request
		try {
			return restTemplate.getForObject(requestUrl, CommunityList.class);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return null;
		}
	}

	/**
	 * Endpoint: Get Top Communities
	 * Calling the getTopCommunities with parameters to get up to limit communities.
	 * Requires Scope: none
	 *
	 * @param limit Maximum number of most-recent objects to return. Default: 25. Maximum: none.
	 * @return The top communities.
	 */
	public List<Community> getTopCommunities(@Nullable Integer limit) {
		return getTopCommunities(limit, null).getCommunities();
	}

	// TODO
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

	public BannedCommunityUsers getCommunityBannedUsers(OAuthCredential credential, String id, @Nullable Integer limit, @Nullable String cursor) {
		try {
			checkScopePermission(credential.getOAuthScopes(), Scope.COMMUNITIES_MODERATE);

			// Endpoint
			String requestUrl = String.format("/communities/%s/bans", id);
			RestTemplate restTemplate = this.restTemplate;

			// Query Parameters
			if (limit != null) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
			}
			if (cursor != null && cursor.equals("")) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("cursor", cursor));
			}

			// Header Parameters
			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.getToken())));

			return restTemplate.getForObject(requestUrl, BannedCommunityUsers.class);
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.getUserId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return null;
		}
	}

	public Boolean banCommunityUser(OAuthCredential credential, String id, Long userId) {
		try {
			checkScopePermission(credential.getOAuthScopes(), Scope.COMMUNITIES_MODERATE);

			// Endpoint
			String requestUrl = String.format("/communities/%s/bans/%s", id, userId);
			RestTemplate restTemplate = this.restTemplate;

			// Parameters
			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.getToken())));

			restTemplate.put(requestUrl, null);

			return true;
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.getUserId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return false;
		}
	}

	public Boolean unbanCommunityUser(OAuthCredential credential, String id, Long userId) {
		try {
			checkScopePermission(credential.getOAuthScopes(), Scope.COMMUNITIES_MODERATE);

			// Endpoint
			String requestUrl = String.format("/communities/%s/bans/%s", id, userId);
			RestTemplate restTemplate = this.restTemplate;

			// Parameters
			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.getToken())));

			restTemplate.delete(requestUrl);

			return true;
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.getUserId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return false;
		}
	}

	/**
	 * Validates community names against the twitch name limitations.
	 *
	 * @param name Community name to validate.
	 * @return Whether the given name is a valid community name.
	 */
	private Boolean validateCommunityName(String name) {
		return name.matches("^[A-Za-z\\-\\_\\.\\~]{3,25}$");
	}
}
