package me.philippheuer.twitch4j.auth;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.auth.model.OAuthRequest;
import ratpack.server.RatpackServer;
import ratpack.server.Stopper;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

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
	 * Will be used to recieve oauth redirect.
	 */
	private Integer localPort = 7090;

	/**
	 * Class Constructor
	 *
	 * @param credentialManager The Credential Manager.
	 */
	public OAuthHandler(CredentialManager credentialManager) {
		setCredentialManager(credentialManager);
		initialize();
	}

	/**
	 * Get server base address for redirect urls
	 *
	 * @return The address of the local web server used for auth handling.
	 */
	public String getServerBaseAddress() {
		return String.format("http://127.0.0.1:%s", getLocalPort().toString());
	}

	/**
	 * Start Listener
	 */
	public void initialize() {
		try {
			ratpackServer = RatpackServer.of(s -> s
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

										// Get Request from Cache
										if(!getCredentialManager().getOAuthRequestCache().containsKey(responseState)) {
											ctx.render("Timeout! Please retry!");
										}
										OAuthRequest request = getCredentialManager().getOAuthRequestCache().get(responseState);

										// Handle Response
										OAuthCredential credential = getCredentialManager().getOAuthTwitch().handleAuthenticationCodeResponseTwitch(responseCode);

										// Valid?
										if (credential != null) {
											// Add requested Scopes to credential (separated by space when more than one is requested)
											if (responseScope.contains(" ")) {
												credential.getOAuthScopes().addAll(Arrays.asList(responseScope.split("\\s")));
											} else {
												credential.getOAuthScopes().add(responseScope);
											}

											// Check for custom key, to store in credential manager
											if (request.getType().length() > 0 && !request.getType().equals("CHANNEL")) {
												// Custom Key
												getCredentialManager().addTwitchCredential(request.getType(), credential);
											} else {
												// Channel Credentials
												getCredentialManager().addTwitchCredential(credential.getUserId().toString(), credential);
											}

											ctx.render("Welcome " + credential.getDisplayName() + "!");
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

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onRequestPermission() {
		// User Requested permission, start the listener for the next 5 minutes
		if (!ratpackServer.isRunning()) {
			try {
				// RatpackServer: Start
				ratpackServer.start();

				final Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						// RatpackServer: Stop
						try {
							if(ratpackServer.isRunning()) {
								ratpackServer.stop();
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}
				}, 360 * 1000);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
