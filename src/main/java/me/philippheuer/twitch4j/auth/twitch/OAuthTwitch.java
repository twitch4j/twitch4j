package me.philippheuer.twitch4j.auth.twitch;

import me.philippheuer.twitch4j.auth.CredentialManager;
import me.philippheuer.twitch4j.auth.twitch.model.TwitchCredential;
import me.philippheuer.twitch4j.model.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.helper.WebsiteUtils;
import me.philippheuer.twitch4j.auth.twitch.model.Authorize;
import me.philippheuer.twitch4j.model.Scopes;

import ratpack.server.*;

import java.util.Optional;

@Getter
@Setter
public class OAuthTwitch {

	/**
	 * API Instance
	 */
	private TwitchClient twitchClient;

	/**
	 * Redirect URL
	 */
	private String redirectUri = "http://127.0.0.1:7090/oauth_authorize";

	/**
	 * Port for local webserver
	 *  Will be used to recieve oauth redirect.
	 */
	private Integer localPort = 7090;

	/**
	 * Constructor
	 */
	public OAuthTwitch(TwitchClient twitchClient) {
		setTwitchClient(twitchClient);
	}

	/**
	 *
	 * @param type Type for Permission for the CredentialManager (IRC/CHANNEL)
	 * @param scopes Scopes to request.
	 * @return Boolean Access Token received?
	 */
	public Optional<TwitchCredential> requestPermissionsFor(String type, Scopes... scopes) {
		// Get OAuthTwitch URI
		String requestUrl = getAuthenticationUrl(scopes);

		// Open Authorization Page for User
		WebsiteUtils.openWebpage(requestUrl);

		// Wait for Response
		Optional<TwitchCredential> twitchCredential = waitForAccessToken();

		if(twitchCredential.isPresent()) {
			// Type: IRC
			if(type.toLowerCase().equals("irc")) {
				getTwitchClient().getCredentialManager().addCredential(CredentialManager.CREDENTIAL_IRC, twitchCredential.get());
			}
			// Type: CHANNEL
			else if(type.toLowerCase().equals("channel")) {
				getTwitchClient().getCredentialManager().addCredential(twitchCredential.get().getUser().getId().toString(), twitchCredential.get());
			}
			// Type: UNKNOWN
			else {

			}
		}

		return twitchCredential;
	}

	/**
     * Returns the authentication URL that you can redirect the user to in order
     * to authorize your application to retrieve an access token.
     *
     * @param scopes	Scopes to request access for
     * @return String	OAuth2 Uri
     */
    private String getAuthenticationUrl(Scopes... scopes) {
        return String.format("%s/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s&force_verify=true",
        		getTwitchClient().getTwitchEndpoint(), getTwitchClient().getClientId(), getRedirectUri(), Scopes.join(scopes), getUniqueState());
    }

    private String getUniqueState() {
    	return "HelloWorld";
    }

    private Optional<TwitchCredential> waitForAccessToken() {
    	final TwitchCredential twitchCredential = new TwitchCredential();

    	try {
    		final RatpackServer ratpackServer = RatpackServer.of(s -> s
					.serverConfig(c -> c
						.port(getLocalPort())
					)
					.handlers(c -> c
						.get(ctx -> ctx.render("Hello World!"))
						//.get(":name", ctx -> ctx.render("Hello " + ctx.getPathTokens().get("name") + "!"))
						.get("oauth_authorize",
							ctx -> {
								// Parse Parameters
								String responseCode = ctx.getRequest().getQueryParams().get("code");
								String responseScope = ctx.getRequest().getQueryParams().get("scope");
								String responseState = ctx.getRequest().getQueryParams().get("state");

								// Handle Response
								twitchCredential.replaceTwitchCredential(handleAuthenticationCodeResponse(responseCode));

								// Show Result Site
								ctx.render("Hello " + responseCode + "!");

								// Response received, close listener
								ctx.onClose(outcome -> {
									new Thread(ctx.get(Stopper.class)::stop).start();
								});
							}
						)
					)
			);

    		ratpackServer.start();

    		// Wait for User
    		while(ratpackServer.isRunning()) {
    			Thread.sleep(200);
			}

    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}

    	if(twitchCredential.getOAuthToken() != null && twitchCredential.getOAuthToken().length() == 0) {
			return Optional.empty();
		}

		return Optional.ofNullable(twitchCredential);
    }

    private TwitchCredential handleAuthenticationCodeResponse(String authenticationCode) {
    	try {
			// Validate on Server
			String requestUrl = String.format("%s/oauth2/token", getTwitchClient().getTwitchEndpoint());
			RestTemplate restTemplate = getTwitchClient().getRestClient().getPlainRestTemplate();

			// Prepare HTTP Post Data
			MultiValueMap<String, Object> postObject = new LinkedMultiValueMap<String, Object>();
			postObject.add("client_id", getTwitchClient().getClientId());
			postObject.add("client_secret", getTwitchClient().getClientSecret());
			postObject.add("grant_type", "authorization_code");
			postObject.add("redirect_uri", getRedirectUri());
			postObject.add("code", authenticationCode);
			postObject.add("state", getUniqueState());

			// Rest Request
			Authorize responseObject = restTemplate.postForObject(requestUrl, postObject, Authorize.class);

			TwitchCredential twitchCredential = new TwitchCredential();
			twitchCredential.setOAuthToken(responseObject.getAccessToken());

			User twitchUser = getTwitchClient().getUserEndpoint().getUser(twitchCredential).get();

			twitchCredential.setUser(twitchUser);

			return twitchCredential;

		} catch (Exception ex) {
    		ex.printStackTrace();

    		return null;
		}
    }
}
