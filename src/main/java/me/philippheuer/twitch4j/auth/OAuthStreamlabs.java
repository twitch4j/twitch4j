package me.philippheuer.twitch4j.auth;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.streamlabs.StreamlabsCredential;
import me.philippheuer.twitch4j.auth.model.twitch.Authorize;
import me.philippheuer.twitch4j.helper.WebsiteUtils;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.auth.model.streamlabs.StreamlabsScopes;
import me.philippheuer.twitch4j.streamlabs.model.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
public class OAuthStreamlabs {

	/**
	 * CredentialManager
	 */
	private CredentialManager credentialManager;

	/**
	 * Redirect KEY
	 */
	public static String REDIRECT_KEY = "oauth_authorize_streamlabs";

	/**
	 * Constructor
	 */
	public OAuthStreamlabs(CredentialManager credentialManager) {
		setCredentialManager(credentialManager);
	}

	/**
	 *
	 * @param type Type for Permission for the CredentialManager (IRC/CHANNEL)
	 * @param streamlabsScopes TwitchScopes to request.
	 */
	public void requestPermissionsFor(String type, StreamlabsScopes... streamlabsScopes) {
		// Get OAuthTwitch URI
		String requestUrl = getAuthenticationUrl(streamlabsScopes);

		// Open Authorization Page for User
		WebsiteUtils.openWebpage(requestUrl);
	}

	/**
     * Returns the authentication URL that you can redirect the user to in order
     * to authorize your application to retrieve an access token.
     *
     * @param streamlabsScopes	StreamlabsScopes to request access for
     * @return String	OAuth2 Uri
     */
    private String getAuthenticationUrl(StreamlabsScopes... streamlabsScopes) {
        return String.format("%s/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=%s",
        		getCredentialManager().getStreamlabsClient().getEndpointUrl(),
				getCredentialManager().getStreamlabsClient().getClientId(),
				getRedirectUri(),
				StreamlabsScopes.join(streamlabsScopes)
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
	public StreamlabsCredential handleAuthenticationCodeResponseStreamlabs(String authenticationCode) {
		try {
			// Validate on Server
			String requestUrl = String.format("%s/token", getCredentialManager().getStreamlabsClient().getEndpointUrl());
			System.out.println(requestUrl);
			RestTemplate restTemplate = getCredentialManager().getStreamlabsClient().getRestClient().getPlainRestTemplate();

			// Prepare HTTP Post Data
			MultiValueMap<String, Object> postObject = new LinkedMultiValueMap<String, Object>();
			postObject.add("client_id", getCredentialManager().getStreamlabsClient().getClientId());
			postObject.add("client_secret", getCredentialManager().getStreamlabsClient().getClientSecret());
			postObject.add("grant_type", "authorization_code");
			postObject.add("redirect_uri", getCredentialManager().getOAuthStreamlabs().getRedirectUri());
			postObject.add("code", authenticationCode);

			// Rest Request
			Authorize responseObject = restTemplate.postForObject(requestUrl, postObject, Authorize.class);

			StreamlabsCredential credential = new StreamlabsCredential();


System.out.println(responseObject.toString());
			credential.setOAuthToken(responseObject.getAccessToken());

			//User twitchUser = getCredentialManager().getTwitchClient().getUserEndpoint().getUser(credential).get();
			//credential.setUser(twitchUser);

			return credential;

		} catch (Exception ex) {
			ex.printStackTrace();

			return null;
		}
	}
}
