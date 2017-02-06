package me.philippheuer.twitch4j.auth;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.twitch.TwitchCredential;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import ratpack.server.RatpackServer;
import ratpack.server.Stopper;

@Getter
@Setter
public class OAuthHandler {
	/**
	 * Holds the Credential Manager
	 */
	private CredentialManager credentialManager;

	/**
	 * Holds the Ratpack Server Instance
	 */
	private RatpackServer ratpackServer;

	/**
	 * Port for local webserver
	 *  Will be used to recieve oauth redirect.
	 */
	private Integer localPort = 7090;

	/**
	 * Constructor
	 */
	public OAuthHandler(CredentialManager credentialManager) {
		setCredentialManager(credentialManager);
	}

	/**
	 * Get server base address for redirect urls
	 */
	public String getServerBaseAddress() {
		return String.format("http://127.0.0.1:%s", getLocalPort().toString());
	}

	/**
	 * Start Listener
	 */
	public void start() {
		try {
			RatpackServer ratpackServer = RatpackServer.of(s -> s
					.serverConfig(c -> c
							.port(getLocalPort())
					)
					.handlers(c -> c
							.get(ctx -> ctx.render("Local OAuth Listener ..."))
							// Twitch
							.get(OAuthTwitch.REDIRECT_KEY,
									ctx -> {
										// Parse Parameters
										String responseCode = ctx.getRequest().getQueryParams().get("code");
										String responseScope = ctx.getRequest().getQueryParams().get("scope");
										String responseState = ctx.getRequest().getQueryParams().get("state");

										System.out.println(responseCode);
										System.out.println(getCredentialManager() != null ? "T" : "F");

										// Handle Response
										TwitchCredential credential = getCredentialManager().getOAuthTwitch().handleAuthenticationCodeResponseTwitch(responseCode);

										// Valid?
										if(credential != null) {
											getCredentialManager().addCredential(credential.getUser().getId().toString(), credential);

											ctx.render("Welcome " + credential.getUser().getDisplayName() + "!");
										} else {
											ctx.render("Authentication failed!");
										}



										// Response received, close listener
										ctx.onClose(outcome -> {
											new Thread(ctx.get(Stopper.class)::stop).start();
										});
									}
							)
							// Streamlabs
							.get(OAuthStreamlabs.REDIRECT_KEY,
									ctx -> {
										// Parse Parameters
										String responseCode = ctx.getRequest().getQueryParams().get("code");
										String responseScope = ctx.getRequest().getQueryParams().get("scope");
										String responseState = ctx.getRequest().getQueryParams().get("state");

										// Handle Response
										credentialManager.getOAuthStreamlabs().handleAuthenticationCodeResponseStreamlabs(responseCode);

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

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
