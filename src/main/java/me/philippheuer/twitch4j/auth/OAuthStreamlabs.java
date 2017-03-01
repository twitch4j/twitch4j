package me.philippheuer.twitch4j.auth;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.auth.model.OAuthRequest;
import me.philippheuer.twitch4j.auth.model.streamlabs.Authorize;
import me.philippheuer.util.desktop.WebsiteUtils;
import me.philippheuer.twitch4j.streamlabs.enums.StreamlabsScopes;
import me.philippheuer.twitch4j.streamlabs.model.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

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
	 * Class Constructor
	 *
	 * @param credentialManager The Credential Manager.
	 */
	public OAuthStreamlabs(CredentialManager credentialManager) {
		setCredentialManager(credentialManager);
	}

	/**
	 * @param type             Type for Permission for the CredentialManager (IRC/CHANNEL)
	 * @param streamlabsScopes TwitchScopes to request.
	 */
	public void requestPermissionsFor(String type, StreamlabsScopes... streamlabsScopes) {
		// Ensures that the listener runs, if permissions are requested
		getCredentialManager().getOAuthHandler().onRequestPermission();

		// Store Request Information / Generate Token
		OAuthRequest request = new OAuthRequest();
		request.setType(type);
		request.getOAuthScopes().addAll(Arrays.stream(streamlabsScopes).map(e -> e.toString()).collect(Collectors.toList()));
		getCredentialManager().getOAuthRequestCache().put(request.getTokenId(), request);

		// Get OAuthTwitch URI
		String requestUrl = getAuthenticationUrl(request.getTokenId(), streamlabsScopes);

		// Open Authorization Page for User
		WebsiteUtils.openWebpage(requestUrl);
	}

	/**
	 * Returns the authentication URL that you can redirect the user to in order
	 * to authorize your application to retrieve an access token.
	 *
	 * @param state            What are the credentials requested for? (CHANNEL/IRC)
	 * @param streamlabsScopes StreamlabsScopes to request access for
	 * @return String    OAuth2 Uri
	 */
	private String getAuthenticationUrl(String state, StreamlabsScopes... streamlabsScopes) {
		return String.format("%s/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=%s&state=%s",
				getCredentialManager().getStreamlabsClient().getEndpointUrl(),
				getCredentialManager().getStreamlabsClient().getClientId(),
				getRedirectUri(),
				StreamlabsScopes.join(streamlabsScopes),
				state
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
	 * @param authenticationCode The oauth token that will be used to access the api.
	 * @return OAuthCredential
	 */
	public OAuthCredential handleAuthenticationCodeResponseStreamlabs(String authenticationCode) {
		try {
			// Validate on Server
			String requestUrl = String.format("%s/token", getCredentialManager().getStreamlabsClient().getEndpointUrl());
			RestTemplate restTemplate = getCredentialManager().getStreamlabsClient().getRestClient().getRestTemplate();

			// Post Data
			MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<String, Object>();
			postParameters.add("grant_type", "authorization_code");
			postParameters.add("client_id", getCredentialManager().getStreamlabsClient().getClientId());
			postParameters.add("client_secret", getCredentialManager().getStreamlabsClient().getClientSecret());
			postParameters.add("redirect_uri", getCredentialManager().getOAuthStreamlabs().getRedirectUri());
			postParameters.add("code", authenticationCode);

			// Rest Request
			Authorize responseObject = restTemplate.postForObject(requestUrl, postParameters, Authorize.class);

			// Credential
			OAuthCredential credential = new OAuthCredential();
			credential.setOAuthToken(responseObject.getAccessToken());
			credential.setOAuthRefreshToken(responseObject.getRefreshToken());

			// Streamlabs - Get User Id
			Optional<User> user = getCredentialManager().getStreamlabsClient().getUserEndpoint(credential).getUser();
			if (user.isPresent()) {
				credential.setUserId(user.get().getId());
				credential.setUserName(user.get().getName());
				credential.setDisplayName(user.get().getDisplayName());
			}

			return credential;

		} catch (Exception ex) {
			ex.printStackTrace();

			return null;
		}
	}
}
