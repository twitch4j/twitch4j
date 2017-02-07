package me.philippheuer.twitch4j.auth;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.streamlabs.StreamlabsCredential;
import me.philippheuer.twitch4j.auth.model.twitch.TwitchCredential;
import me.philippheuer.twitch4j.auth.model.twitch.TwitchScopes;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import ratpack.server.RatpackServer;
import ratpack.server.Stopper;

import java.util.Arrays;

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
		start();
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

										// Handle Response
										TwitchCredential credential = getCredentialManager().getOAuthTwitch().handleAuthenticationCodeResponseTwitch(responseCode);

										// Valid?
										if(credential != null) {
											// Add requested Scopes to credential (separated by space when more than one is requested)
											if(responseScope.contains(" ")) {
												credential.getOAuthScopes().addAll(Arrays.asList(responseScope.split("\\s")));
											} else {
												credential.getOAuthScopes().add(responseScope);
											}

											// Store Credential
											if(responseState.equals("IRC")) {
												// IRC Credentials
												getCredentialManager().addCredential(CredentialManager.CREDENTIAL_IRC, credential);
											} else {
												// Channel Credentials
												getCredentialManager().addCredential(credential.getUser().getId().toString(), credential);
											}

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

										// Handle Response
										StreamlabsCredential credential = credentialManager.getOAuthStreamlabs().handleAuthenticationCodeResponseStreamlabs(responseCode);

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
					)
			);

			ratpackServer.start();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
