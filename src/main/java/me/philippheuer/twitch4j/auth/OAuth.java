package me.philippheuer.twitch4j.auth;

import org.springframework.web.client.RestTemplate;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.helper.WebsiteUtils;
import me.philippheuer.twitch4j.auth.model.Authorize;
import me.philippheuer.twitch4j.auth.model.AuthorizeRequest;
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
		String requestUrl = getAuthenticationUrl(Scopes.USER_SUBSCRIPTIONS);

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
        return String.format("%s/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s",
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

								ctx.render("Hello " + ctx.getPathTokens().get("code") + "!");
								System.out.println(ctx.getRequest().getQueryParams().toString());

								handleAuthenticationCodeResponse(ctx.getPathTokens().get("code"));

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
    	String requestUrl = String.format("https://api.twitch.tv/kraken/oauth2/token");
    	RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

    	AuthorizeRequest authorizeRequest = new AuthorizeRequest(getTwitchClient().getClientId(), getTwitchClient().getClientSecret(), "authorization_code", getRedirectUri(), authenticationCode, getUniqueState());
		Authorize responseObject = restTemplate.postForObject(requestUrl, authorizeRequest, Authorize.class);

		System.out.println(responseObject.toString());
		if(responseObject.getAccessToken() != null) {
			System.out.println("Success?!");
		}
    }
}
