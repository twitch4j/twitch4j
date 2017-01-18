package me.philippheuer.twitch4j.auth.twitch;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.helper.WebsiteUtils;
import me.philippheuer.twitch4j.auth.twitch.model.Authorize;
import me.philippheuer.twitch4j.model.Scopes;

import ratpack.server.*;

@Getter
@Setter
public class OAuth {

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
	public OAuth(TwitchClient api) {
		setTwitchClient(api);
	}

	/**
	 *
	 * @return Boolean Access Token received?
	 */
	public Boolean startLocalAuthorization() {
		// Get OAuth URI
		String requestUrl = getAuthenticationUrl(Scopes.USER_READ, Scopes.USER_SUBSCRIPTIONS);

		// Open Authorization Page for User
		WebsiteUtils.openWebpage(requestUrl);

		// Wait for Response
		waitForAccessToken();


		return true;
	}

	/**
     * Returns the authentication URL that you can redirect the user to in order
     * to authorize your application to retrieve an access token.
     *
     * @param scopes	Scopes to request access for
     * @return String	OAuth2 Uri
     */
    public String getAuthenticationUrl(Scopes... scopes) {
        return String.format("%s/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s&force_verify=true",
        		getTwitchClient().getTwitchEndpoint(), getTwitchClient().getClientId(), getRedirectUri(), Scopes.join(scopes), getUniqueState());
    }

    private String getUniqueState() {
    	return "HelloWorld";
    }

    public void waitForAccessToken() {
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
								handleAuthenticationCodeResponse(responseCode);

								// Show Result Site
								ctx.render("Hello " + responseCode + "!");

								// Response received, close listener
								ctx.onClose(outcome -> new Thread(ctx.get(Stopper.class)::stop).start());
							}
						)
					)
			);

    		ratpackServer.start();

    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }

    private void handleAuthenticationCodeResponse(String authenticationCode) {
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

		System.out.println(responseObject.toString());
		if(responseObject.getAccessToken() != null) {
			// Success
		}
    }
}
