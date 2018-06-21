package twitch4j.api.kraken.endpoints;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;
import twitch4j.api.kraken.model.Token;
import twitch4j.api.kraken.model.TokenResponse;
import twitch4j.api.util.rest.HeaderRequestInterceptor;
import twitch4j.common.auth.ICredential;

@Slf4j
public class KrakenEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Class Constructor
	 *
	 * @param restTemplate The Twitch Client.
	 */
	public KrakenEndpoint(RestTemplate restTemplate) {
		super(restTemplate);
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
	public Token getToken(ICredential credential) {
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.accessToken())));

		// REST Request
		try {
			return restTemplate.getForObject("/", TokenResponse.class).getToken();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			// Default Response: Invalid Token
			return new Token();
		}
	}

	/**
	 * Gets/refreshes the token
	 *
	 * @param grant_type   Valid values: authorization_code or refresh_token.
	 * @param redirect_url Redirect url.
	 * @param code         authentication_code or refresh_token
	 * @return {@link ICredential} data on {@link Optional} container class
	 * @deprecated use {@link twitch4j.common.auth.CredentialManager#authorize(String)} and put only authorization code
	 */
	@Deprecated
	public Optional<ICredential> getOAuthToken(String grant_type, String redirect_url, String code) {
		return Optional.empty();
	}

}
