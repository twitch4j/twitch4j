package me.philippheuer.twitch4j;

import lombok.*;
import lombok.experimental.Wither;
import me.philippheuer.twitch4j.auth.CredentialManager;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Builder to get a TwitchClient Instance by provided various options, to provide the user with a lot of customizable options.
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@Wither
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchClientBuilder {

	/**
	 * Client ID
	 */
	private String clientId;

	/**
	 * Client Secret
	 */
	private String clientSecret;

	/**
	 * IRC Credential
	 */
	private String credential;

	/**
	 * IRC Credential - Refresh Token
	 */
	private String credentialRefreshToken;

	/**
	 * Auto Saving Configuration
	 */
	private boolean autoSaveConfiguration = false;

	/**
	 * Configuration Directory
	 */
	private File configurationDirectory;

	/**
	 * List of listeners
	 */
	private final Set<Object> listeners = new HashSet<>();

	/**
	 * Initializing builder
	 * @return Client Builder
	 */
	public static TwitchClientBuilder init() {
		return new TwitchClientBuilder();
	}

	/**
	 * Initialize
	 * @return {@link TwitchClient} initialized class
	 */
	public TwitchClient build() {
		// Reqired Parameters
		Assert.notNull(clientId, "You need to provide a client id!");
		Assert.notNull(clientSecret, "You need to provide a client secret!");

		final TwitchClient client = new TwitchClient(clientId, clientSecret);
		client.getCredentialManager().provideTwitchClient(client);
		client.getCredentialManager().setSaveCredentials(autoSaveConfiguration);
		if (configurationDirectory != null) {
			if (!configurationDirectory.exists()) {
				configurationDirectory.mkdirs();
			}

			client.setConfigurationDirectory(configurationDirectory);
			client.getCredentialManager().initializeConfiguration();
			client.getCommandHandler().initializeConfiguration();
		}

		if (credential != null) {
			OAuthCredential oAuthCredential = new OAuthCredential((credential.toLowerCase().startsWith("oauth:")) ? credential.substring(6) : credential);

			// got a refresh token?
			if(credentialRefreshToken != null) {
				oAuthCredential.setRefreshToken(credentialRefreshToken);
				oAuthCredential.setTokenExpiresAt(Calendar.getInstance());
			}

			client.getCredentialManager().addTwitchCredential(CredentialManager.CREDENTIAL_IRC, oAuthCredential);
		}

		if (listeners.size() > 0) {
			listeners.forEach(client.getDispatcher()::registerListener);
		}

		return client;
	}

	public TwitchClientBuilder withListener(Object listener) {
		listeners.add(listener);
		return this;
	}

	/**
	 * Initialize and connect to twitch
	 * @return {@link TwitchClient} initialized class
	 */
	public TwitchClient connect() {
		Assert.notNull(credential, "You need provide a OAuth Credentials for the Bot. Use: https://twitchapps.com/tmi/ to generate a oauth key.");
		TwitchClient client = build();
		client.connect();
		return client;
	}
}
