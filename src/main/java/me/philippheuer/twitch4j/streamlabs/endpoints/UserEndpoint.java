package me.philippheuer.twitch4j.streamlabs.endpoints;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.helper.QueryRequestInterceptor;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.streamlabs.model.DonationList;
import me.philippheuer.twitch4j.auth.model.streamlabs.StreamlabsCredential;
import me.philippheuer.twitch4j.streamlabs.model.User;
import me.philippheuer.twitch4j.streamlabs.model.UserResponse;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class UserEndpoint extends AbstractStreamlabsEndpoint {

	/**
	 * Donation Endpoint
	 */
	public UserEndpoint(StreamlabsClient streamlabsClient) {
		super(streamlabsClient);
	}

	/**
	 * Endpoint: Get the Streamlabs User
	 *  Fetch information about the authenticated user.
	 * Requires Scope: none
	 */
	public Optional<User> getUser(StreamlabsCredential credential) {
		// REST Request
		try {
			// Prepare
			String requestUrl = String.format("%s/user?access_token=%s", getStreamlabsClient().getEndpointUrl(), credential.getOAuthToken());
			RestTemplate restTemplate = getStreamlabsClient().getRestClient().getRestTemplate();
			System.out.println(credential.toString());

			// Request
			UserResponse responseObject = restTemplate.getForObject(requestUrl, UserResponse.class);

			return Optional.ofNullable(responseObject.getTwitch());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
}
