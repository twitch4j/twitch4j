package me.philippheuer.twitch4j.auth;

import me.philippheuer.twitch4j.auth.model.twitch.TwitchCredential;
import me.philippheuer.twitch4j.auth.model.twitch.TwitchScopes;
import me.philippheuer.twitch4j.model.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.helper.WebsiteUtils;
import me.philippheuer.twitch4j.auth.model.twitch.Authorize;

import ratpack.server.*;

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
	 *
	 * @param type Type for Permission for the CredentialManager (IRC/CHANNEL)
	 * @param twitchScopes TwitchScopes to request.
	 */
	public void requestPermissionsFor(String type, TwitchScopes... twitchScopes) {
		// Get OAuthTwitch URI
		String requestUrl = getAuthenticationUrl(twitchScopes);

		// Open Authorization Page for User
		WebsiteUtils.openWebpage(requestUrl);
	}

	/**
     * Returns the authentication URL that you can redirect the user to in order
     * to authorize your application to retrieve an access token.
     *
     * @param scopes	TwitchScopes to request access for
     * @return String	OAuth2 Uri
     */
    private String getAuthenticationUrl(TwitchScopes... scopes) {
        return String.format("%s/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&force_verify=true",
				getCredentialManager().getTwitchClient().getTwitchEndpoint(),
				getCredentialManager().getTwitchClient().getClientId(),
				getRedirectUri(),
				TwitchScopes.join(scopes)
		);
    }

	/**
	 * Returns the redirect URL a user is redirected to after a successful authorization.
	 * @return String	Redirect Uri
	 */
	public String getRedirectUri() {
		return String.format("%s/%s",
				getCredentialManager().getOAuthHandler().getServerBaseAddress(),
				REDIRECT_KEY
		);
	}

	/**
	 * Handle Authentication Response
	 * @param authenticationCode
	 * @return TwitchCredential
	 */
	public TwitchCredential handleAuthenticationCodeResponseTwitch(String authenticationCode) {
		try {
			// Validate on Server
			String requestUrl = String.format("%s/oauth2/token", getCredentialManager().getTwitchClient().getTwitchEndpoint());
			RestTemplate restTemplate = getCredentialManager().getTwitchClient().getRestClient().getPlainRestTemplate();

			// Prepare HTTP Post Data
			MultiValueMap<String, Object> postObject = new LinkedMultiValueMap<String, Object>();
			postObject.add("client_id", getCredentialManager().getTwitchClient().getClientId());
			postObject.add("client_secret", getCredentialManager().getTwitchClient().getClientSecret());
			postObject.add("grant_type", "authorization_code");
			postObject.add("redirect_uri", getCredentialManager().getOAuthTwitch().getRedirectUri());
			postObject.add("code", authenticationCode);

			// Rest Request
			Authorize responseObject = restTemplate.postForObject(requestUrl, postObject, Authorize.class);

			TwitchCredential twitchCredential = new TwitchCredential();
			twitchCredential.setOAuthToken(responseObject.getAccessToken());

			User twitchUser = getCredentialManager().getTwitchClient().getUserEndpoint().getUser(twitchCredential).get();

			twitchCredential.setUser(twitchUser);

			return twitchCredential;

		} catch (Exception ex) {
			ex.printStackTrace();

			return null;
		}
	}
}
