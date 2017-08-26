package me.philippheuer.twitch4j;

import lombok.Setter;
import me.philippheuer.twitch4j.auth.CredentialManager;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import org.springframework.util.Assert;

import java.io.File;

/**
 * Builder to get a TwitchClient Instance by provided various options, to provide the user with a lot of customizable options.
 */
@Setter
public class TwitchClientBuilder {

	private String clientId;
	private String clientSecret;
	private String ircCredential;
	private boolean autoSaveConfiguration = false;
	private File configurationDirectory;

	public static TwitchClientBuilder init() {
		return new TwitchClientBuilder();
	}

	public void setConfigurationDirectory(String directory) {
		setConfigurationDirectory(new File(directory));
	}

	public void setConfigurationDirectory(File directory) {
		configurationDirectory = directory;
	}

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

	public TwitchClient login() {
		TwitchClient client = build();
		client.connect();
		return client;
	}
}
