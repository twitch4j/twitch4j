package me.philippheuer.twitch4j.auth;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.streamlabs.StreamlabsCredential;
import me.philippheuer.twitch4j.auth.model.streamlabs.Authorize;
import me.philippheuer.twitch4j.helper.WebsiteUtils;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.auth.model.streamlabs.StreamlabsScopes;
import me.philippheuer.twitch4j.streamlabs.model.User;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

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
			System.out.println(authenticationCode);
			RestTemplate restTemplate = getCredentialManager().getStreamlabsClient().getRestClient().getRestTemplate();

			// Headers
			HttpHeaders postHeaders = new HttpHeaders();
			// postHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			// postHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.85 Safari/537.36");

			// Post Data
			MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<String, Object>();
			postParameters.add("grant_type", "authorization_code");
			postParameters.add("client_id", getCredentialManager().getStreamlabsClient().getClientId());
			postParameters.add("client_secret", getCredentialManager().getStreamlabsClient().getClientSecret());
			postParameters.add("redirect_uri", getCredentialManager().getOAuthStreamlabs().getRedirectUri());
			postParameters.add("code", authenticationCode);
			System.out.println(postParameters.toString());

			// Rest Request
			Authorize responseObject = restTemplate.postForObject(requestUrl, postParameters, Authorize.class);

			// Credential
			StreamlabsCredential credential = new StreamlabsCredential();
			credential.setOAuthToken(responseObject.getAccessToken());
			credential.setOAuthRefreshToken(responseObject.getRefreshToken());

			// Streamlabs - Get User Id
			Optional<User> user = getCredentialManager().getStreamlabsClient().getUserEndpoint().getUser(credential);
			if(user.isPresent()) {
				credential.setUser(user.get());
			} else {
				// Logger.error
			}

			System.out.println(credential.toString());

			return credential;

		} catch (Exception ex) {
			ex.printStackTrace();

			return null;
		}
	}
}
