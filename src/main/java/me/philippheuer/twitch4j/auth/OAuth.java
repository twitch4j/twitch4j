package me.philippheuer.twitch4j.auth;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.model.Scopes;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;

@Getter
@Setter
public class OAuth {

	/**
	 * API Instance
	 */
	private TwitchClient api;
	
	/**
	 * Redirect URL
	 */
	private String redirectUri = "http://127.0.0.1:7090/oauth/";
	
	/**
	 * Port for local webserver
	 *  Will be used to recieve oauth redirect.
	 */
	private Integer localPort = 7090;
	
	/**
	 * Constructor
	 */
	public OAuth(TwitchClient api) {
		setApi(api);
	}
	
	/**
     * Returns the authentication URL that you can redirect the user to in order
     * to authorize your application to retrieve an access token.
     *
     * @param scopes	Scopes to request access for
     * @return String	OAuth2 Uri
     */
    public String getAuthenticationUrl(Scopes... scopes) {
        return String.format("%s/oauth2/authorize?response_type=token&client_id=%s&redirect_uri=%s&scope=%s",
        		getApi().getTwitchEndpoint(), getApi().getClientId(), getRedirectUri(), Scopes.join(scopes));
    }
    
    
    public void waitForAccessToken() {
    	try {
    		RatpackServer.start(s -> s
				.serverConfig(c -> c
					.baseDir(BaseDir.find())
					.env()
					.port(getLocalPort())
				)
				.handlers(c -> c
					.get(ctx -> ctx.render("Hello World!"))
		        	//.get(":name", ctx -> ctx.render("Hello " + ctx.getPathTokens().get("name") + "!"))
		        	.get("oauth2/authorize?response_type=token&client_id=:clientId&redirect_uri=:redirectUri&scope=Scope:",
		        		ctx -> ctx.render("Hello " + ctx.getPathTokens().get("name") + "!"))
				)
		    );
    	} catch (Exception ex) {
    		
    	}
    }
    
    /**
     * Listens for callback from Twitch server with the access token.
     * <code>getAuthenticationUrl()</code> must be called prior to this function!
     *
     * @param authUrl    the URL to a custom authorize page.
     * @param successUrl the URL to a custom successful authentication page.
     * @param failUrl    the URL to a custom failed authentication page.
     * @return
     */
    /*
    public boolean awaitAccessToken(URL authUrl, URL successUrl, URL failUrl) {
        if (clientId == null || redirectUri == null) return false;

        AuthenticationCallbackServer server = new AuthenticationCallbackServer(listenPort, authUrl, successUrl, failUrl);
        try {
            server.start();
        } catch (IOException e) {
            authenticationError = new AuthenticationError("JavaException", e.toString());
            return false;
        }

        if (server.hasAuthenticationError() || server.getAccessToken() == null) {
            authenticationError = server.getAuthenticationError();
            return false;
        }

        accessToken = server.getAccessToken();
        return true;
    }
    */
}
