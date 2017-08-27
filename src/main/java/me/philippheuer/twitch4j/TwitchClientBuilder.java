package me.philippheuer.twitch4j;

import lombok.Setter;
import lombok.experimental.Accessors;
import me.philippheuer.twitch4j.auth.CredentialManager;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import org.springframework.util.Assert;

import java.io.File;

/**
 * Builder to get a TwitchClient Instance by provided various options, to provide the user with a lot of customizable options.
 * @author Damian Staszewski
 */
@Setter
@Accessors(chain = true)
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
	private String ircCredential;
	/**
	 * Auto Saving Configuration
	 */
	private boolean autoSaveConfiguration = false;
	/**
	 * Configuration Directory
	 */
	private File configurationDirectory;

	/**
	 * Initializing builder
	 * @return Client Builder
	 */
	public static TwitchClientBuilder init() {
		return new TwitchClientBuilder();
	}

	/**
	 * Builder
	 */
	private TwitchClientBuilder() {}

	/**
	 * Setting Configuration Directory
	 * @param directory directory name
	 */
	public void setConfigurationDirectory(String directory) {
		setConfigurationDirectory(new File(directory));
	}

	/**
	 * Setting Configuration Directory
	 * @param directory {@link File} directory
	 */
	public void setConfigurationDirectory(File directory) {
		configurationDirectory = directory;
	}

	/**
	 * Initialization
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
			if (!configurationDirectory.exists()) configurationDirectory.mkdirs();
			client.setConfigurationDirectory(configurationDirectory);
			client.getCredentialManager().initializeConfiguration();
			client.getCommandHandler().initializeConfiguration();
		}

		if (ircCredential != null)
			client.getCredentialManager().addTwitchCredential(CredentialManager.CREDENTIAL_IRC, new OAuthCredential(ircCredential));

		return client;
	}

	/**
	 * Initialization with connecting to the chat
	 * @return {@link TwitchClient} initialized class
	 */
	public TwitchClient login() {
		TwitchClient client = build();
		client.connect();
		return client;
	}
}
