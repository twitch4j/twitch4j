package me.philippheuer.twitch4j.auth;

import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import me.philippheuer.twitch4j.model.Token;
import me.philippheuer.twitch4j.model.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.*;
import me.philippheuer.twitch4j.helper.WebsiteUtils;
import me.philippheuer.twitch4j.auth.model.twitch.Authorize;

import java.util.Optional;

@Getter
@Setter
public class OAuthTwitch {

	/**
	 * CredentialManager
	 */
	private CredentialManager credentialManager;

	/**
	 * Redirect KEY
	 */
	public static String REDIRECT_KEY = "oauth_authorize_twitch";

	/**
	 * Constructor
	 */
	protected OAuthTwitch(CredentialManager credentialManager) {
		setCredentialManager(credentialManager);
	}

	/**
	 * @param type         Type for Permission for the CredentialManager (IRC/CHANNEL)
	 * @param twitchScopes TwitchScopes to request.
	 */
	public void requestPermissionsFor(String type, TwitchScopes... twitchScopes) {
		// Ensures that the listener runs, if permissions are requested
		getCredentialManager().getOAuthHandler().onRequestPermission();

		// Get OAuthTwitch URI
		String requestUrl = getAuthenticationUrl(type, twitchScopes);

		// Open Authorization Page for User
		WebsiteUtils.openWebpage(requestUrl);
	}

	/**
	 * Returns the authentication URL that you can redirect the user to in order
	 * to authorize your application to retrieve an access token.
	 *
	 * @param type   What are the credentials requested for? (CHANNEL/IRC)
	 * @param scopes TwitchScopes to request access for
	 * @return String    OAuth2 Uri
	 */
	private String getAuthenticationUrl(String type, TwitchScopes... scopes) {
		return String.format("%s/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s&force_verify=true",
				getCredentialManager().getTwitchClient().getTwitchEndpoint(),
				getCredentialManager().getTwitchClient().getClientId(),
				getRedirectUri(),
				TwitchScopes.join(scopes),
				type
		);
	}

	/**
	 * Returns the redirect URL a user is redirected to after a successful authorization.
	 *
	 * @return String    Redirect Uri
	 */
	public String getRedirectUri() {
		return String.format("%s/%s",
				getCredentialManager().getOAuthHandler().getServerBaseAddress(),
				REDIRECT_KEY
		);
	}

	/**
	 * Handle Authentication Response
	 *
	 * @param authenticationCode
	 * @return OAuthCredential
	 */
	public OAuthCredential handleAuthenticationCodeResponseTwitch(String authenticationCode) {
		try {
			// Validate on Server
			String requestUrl = String.format("%s/oauth2/token", getCredentialManager().getTwitchClient().getTwitchEndpoint());
			RestTemplate restTemplate = getCredentialManager().getTwitchClient().getRestClient().getRestTemplate();

			// Prepare HTTP Post Data
			MultiValueMap<String, Object> postObject = new LinkedMultiValueMap<String, Object>();
			postObject.add("client_id", getCredentialManager().getTwitchClient().getClientId());
			postObject.add("client_secret", getCredentialManager().getTwitchClient().getClientSecret());
			postObject.add("grant_type", "authorization_code");
			postObject.add("redirect_uri", getCredentialManager().getOAuthTwitch().getRedirectUri());
			postObject.add("code", authenticationCode);

			// Rest Request
			Authorize responseObject = restTemplate.postForObject(requestUrl, postObject, Authorize.class);

			// Credential
			OAuthCredential credential = new OAuthCredential();
			credential.setOAuthToken(responseObject.getAccessToken());

			// Get Token Status from Kraken Endpoint
			Token token = getCredentialManager().getTwitchClient().getKrakenEndpoint().getToken(credential);
			if(token.getValid()) {
				credential.setUserId(token.getUserId());
				credential.setUserName(token.getUserName());
				credential.setDisplayName(token.getUserName());
			}

			return credential;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
