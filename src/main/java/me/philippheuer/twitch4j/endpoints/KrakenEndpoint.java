package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.Token;
import me.philippheuer.twitch4j.model.TokenResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
public class KrakenEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Class Constructor
	 *
	 * @param twitchClient The TwitchClient.
	 */
	public KrakenEndpoint(TwitchClient twitchClient) {
		super(twitchClient);
	}

	/**
	 * Endpoint: Get OAuth Token Information
	 * Gets information about the provided oAuthToken
	 * Requires Scope: none
	 *
	 * @param credential The credential the information should be fetched for.
	 * @return Information about the user, that issued the token - also provides info about scopes/valid/etc.
	 * @see Token
	 */
	public Token getToken(OAuthCredential credential) {
		// Endpoint
		String requestUrl = String.format("%s", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(credential);

		// REST Request
		try {
			TokenResponse responseObject = restTemplate.getForObject(requestUrl, TokenResponse.class);

			return responseObject.getToken();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		// Default Response: Invalid Token
		return new Token();
	}

}
