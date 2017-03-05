package me.philippheuer.twitch4j.streamlabs.endpoints;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.streamlabs.model.User;
import me.philippheuer.twitch4j.streamlabs.model.UserResponse;
import me.philippheuer.util.rest.QueryRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Getter
@Setter
public class UserEndpoint extends AbstractStreamlabsEndpoint {

	/**
	 * Holds the credentials to the current user
	 */
	private OAuthCredential oAuthCredential;

	/**
	 * Stream Labs - Authenticated Endpoint
	 *
	 * @param streamlabsClient todo
	 * @param credential       todo
	 */
	public UserEndpoint(StreamlabsClient streamlabsClient, OAuthCredential credential) {
		super(streamlabsClient);
		setOAuthCredential(credential);
	}

	/**
	 * Endpoint: Get the Streamlabs User
	 * Fetch information about the authenticated user.
	 * Requires Scope: none
	 *
	 * @return Returns an optional of type user
	 */
	public Optional<User> getUser() {
		// Endpoint
		String requestUrl = String.format("%s/user", getStreamlabsClient().getEndpointUrl());
		RestTemplate restTemplate = getStreamlabsClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("access_token", getOAuthCredential().getToken()));

		// REST Request
		try {
			UserResponse responseObject = restTemplate.getForObject(requestUrl, UserResponse.class);

			return Optional.ofNullable(responseObject.getTwitch());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

}
