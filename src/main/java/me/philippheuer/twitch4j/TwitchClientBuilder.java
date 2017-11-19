package me.philippheuer.twitch4j;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.philippheuer.twitch4j.auth.CredentialManager;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import org.springframework.util.Assert;

import java.io.File;

/**
 * Builder to get a TwitchClient Instance by provided various options, to provide the user with a lot of customizable options.
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
@Setter
@NoArgsConstructor
@Accessors(chain = true, prefix = "with")
public class TwitchClientBuilder {

	/**
	 * Client ID
	 */
	private String clientId;

	/**
	 * Client Secret
	 */
	private String secret;

	/**
	 * IRC Credential
	 */
	private String credential;

	/**
	 * Auto Saving Configuration
	 */
	private boolean autoSaveConfiguration = false;

	/**
	 * Configuration Directory
	 */
	private File configurationDirectory;

	/**
	 * StreamLabs Client
	 * TODO: Remove
	 */
	private StreamlabsClient streamlabsClient = null;

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
		Assert.notNull(secret, "You need to provide a client secret!");

		final TwitchClient client = new TwitchClient(clientId, secret);
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

		if (credential != null)
		{
			client.getCredentialManager().addTwitchCredential(CredentialManager.CREDENTIAL_IRC,
					new OAuthCredential((credential.toLowerCase().startsWith("oauth:")) ? credential.substring(6) : credential));
		}

		// Streamlabs
		if(streamlabsClient != null) {
			client.setStreamLabsClient(streamlabsClient);
			client.getCredentialManager().provideStreamlabsClient(client.getStreamLabsClient());
		}

		return client;
	}

	/**
	 * Initialize and connect to twitch
	 * @return {@link TwitchClient} initialized class
	 */
	public TwitchClient connect() {
		Assert.notNull(credential, "You need provide a OAuth Credentials for Bot. Use: https://twitchapps.com/tmi/ to generate oauth key");
		TwitchClient client = build();
		client.connect();
		return client;
	}
}
