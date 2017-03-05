package me.philippheuer.twitch4j.streamlabs.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.auth.model.streamlabs.Authorize;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.streamlabs.model.User;
import me.philippheuer.twitch4j.streamlabs.model.UserResponse;
import me.philippheuer.util.rest.QueryRequestInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Getter
@Setter
public class TokenEndpoint extends AbstractStreamlabsEndpoint {

	/**
	 * Stream Labs - Authenticated Endpoint
	 *
	 * @param streamlabsClient todo
	 */
	public TokenEndpoint(StreamlabsClient streamlabsClient) {
		super(streamlabsClient);
	}

	/**
	 * Gets/refreshes the token
	 *
	 * @param grant_type Valid values: authorization_code or refresh_token.
	 * @param redirect_url Redirect url.
	 * @param code authentication_code or refresh_token
	 * @return
	 */
	public Optional<Authorize> getToken(String grant_type, String redirect_url, String code) {
		// Endpoint
		String requestUrl = String.format("%s/token", getStreamlabsClient().getEndpointUrl());
		RestTemplate restTemplate = getStreamlabsClient().getRestClient().getRestTemplate();

		// Post Data
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<String, Object>();
		postParameters.add("grant_type", grant_type);
		postParameters.add("client_id", getStreamlabsClient().getClientId());
		postParameters.add("client_secret", getStreamlabsClient().getClientSecret());
		postParameters.add("redirect_uri", redirect_url);
		if(grant_type.equals("authorization_code")) {
			postParameters.add("code", code);
		} else if(grant_type.equals("refresh_token")) {
			postParameters.add("refresh_token", code);
		}

		// REST Request
		try {
			Authorize responseObject = restTemplate.postForObject(requestUrl, postParameters, Authorize.class);

			return Optional.ofNullable(responseObject);
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return Optional.empty();
	}

}
