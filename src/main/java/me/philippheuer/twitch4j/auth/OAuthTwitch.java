package me.philippheuer.twitch4j.auth;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.auth.model.OAuthRequest;
import me.philippheuer.twitch4j.auth.model.twitch.Authorize;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.system.AuthTokenExpiredEvent;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.Token;
import me.philippheuer.util.desktop.WebsiteUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Collectors;

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
	 * Class Constructor
	 *
	 * @param credentialManager The Credential Manager.
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

		// Store Request Information / Generate Token
		OAuthRequest request = new OAuthRequest();
		request.setType(type);
		request.getOAuthScopes().addAll(Arrays.stream(twitchScopes).map(e -> e.toString()).collect(Collectors.toList()));
		getCredentialManager().getOAuthRequestCache().put(request.getTokenId(), request);

		// Get OAuthTwitch URI
		String requestUrl = getAuthenticationUrl(request.getTokenId(), twitchScopes);

		// Open Authorization Page for User
		WebsiteUtils.openWebpage(requestUrl);
	}

	/**
	 * Returns the authentication URL that you can redirect the user to in order
	 * to authorize your application to retrieve an access token.
	 *
	 * @param state   What are the credentials requested for? (CHANNEL/IRC)
	 * @param scopes TwitchScopes to request access for
	 * @return String    OAuth2 Uri
	 */
	private String getAuthenticationUrl(String state, TwitchScopes... scopes) {
		return String.format("%s/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s&force_verify=true",
				Endpoints.API.getURL(),
				getCredentialManager().getTwitchClient().getClientId(),
				getRedirectUri(),
				TwitchScopes.join(scopes),
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
	public OAuthCredential handleAuthenticationCodeResponseTwitch(String authenticationCode) {
		try {
			// Validate on Server
			Optional<Authorize> responseObject = getCredentialManager().getTwitchClient().getKrakenEndpoint().getOAuthToken("authorization_code", getRedirectUri(), authenticationCode);
			if(!responseObject.isPresent()) {
				throw new Exception("Invalid Code!");
			}

			// Credential
			OAuthCredential credential = new OAuthCredential();
			credential.setType("twitch");
			credential.setToken(responseObject.get().getAccessToken());
			credential.setRefreshToken(responseObject.get().getRefreshToken());

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, 3600);
			credential.setTokenExpiresAt(calendar);

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

	/**
	 * Event that gets triggered when a streamlabs token is expired.
	 * <p>
	 -
	 * This events get triggered when a streamlabs auth token has expired, a new token
	 * will be requested using the refresh token.
	 *
	 * @param event The Event, containing the credential and all other related information.
	 */
	@EventSubscriber
	public void onTokenExpired(AuthTokenExpiredEvent event) {
		// Filter to Streamlabs credentials
		if(event.getCredential().getType().equals("twitch")) {
			OAuthCredential credential = event.getCredential();
			System.out.println("OLD: " + credential.toString());

			// Rest Request to get refreshed token details
			Authorize responseObject = getCredentialManager().getTwitchClient().getKrakenEndpoint().getOAuthToken(
					"refresh_token",
					getRedirectUri(),
					credential.getRefreshToken()
			).get();
			System.out.println("NEW: " + responseObject.toString());

			// Save Response
			credential.setToken(responseObject.getAccessToken());
			credential.setRefreshToken(responseObject.getRefreshToken());

			// Set Token Expiry Date
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, 3600);
			credential.setTokenExpiresAt(calendar);

			// Credential was modified.
			getCredentialManager().onCredentialChanged();
		}
	}
}
