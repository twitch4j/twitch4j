package me.philippheuer.twitch4j.auth;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.twitch.OAuthTwitch;
import me.philippheuer.twitch4j.auth.twitch.model.TwitchCredential;
import me.philippheuer.twitch4j.model.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class CredentialManager {

	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(CredentialManager.class);

	/**
	 * Holds the API Instance
	 */
	private TwitchClient twitchClient;

	/**
	 * Save all credentials locally be default?
	 */
	private Boolean saveCredentials = false;

	// IRC Key
	public static String CREDENTIAL_IRC = "IRC";

	private final Map<String, TwitchCredential> oAuthCredentials = new HashMap<>();

	/**
	 * Holds the credentials
	 */
	private File credentialFile;

	/**
	 * Constructor
	 */
	public CredentialManager(TwitchClient twitchClient) {
		super();

		setTwitchClient(twitchClient);
	}

	/**
	 * Adds Twitch Credentials to the Credential Manager
	 * @param key Key
	 * @param twitchCredential Credential Instance
	 */
	public void addCredential(String key, TwitchCredential twitchCredential) {
		// Remove value if it exists
		if(getOAuthCredentials().containsKey(key)) {
			getLogger().debug(String.format("Credentials with key [%s] already present in CredentialManager.", key));
			getOAuthCredentials().remove(key);
		}

		// Add value
		getLogger().debug(String.format("Added Credentials with key [%s] and data [%s]", key, twitchCredential.toString()));

		getOAuthCredentials().put(key, twitchCredential);

		// Store
		saveToFile();
	}

	/**
	 * Get Twitch credentials for channel
	 * @param channel Channel
	 * @return Optional<TwitchCredential> credential with oauth token and access scope.
	 */
	public Optional<TwitchCredential> getForChannel(Channel channel) {
		if(getOAuthCredentials().containsKey(channel.getId().toString())) {
			return Optional.ofNullable(getOAuthCredentials().get(channel.getId().toString()));
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Get Twitch credentials for irc
	 * @return Optional<TwitchCredential> credential with oauth token and access scope.
	 */
	public Optional<TwitchCredential> getForIRC() {
		return Optional.ofNullable(getOAuthCredentials().get(CREDENTIAL_IRC));
	}

	public OAuthTwitch getTwitchOAuth() {
		return new OAuthTwitch(getTwitchClient());
	}

	public void configurationCreate() {
		// Ensure that the file exists
		try {
			File file = new File(getTwitchClient().getConfigurationDirectory().getAbsolutePath() + File.separator + "credentials.json");
			file.createNewFile();
			setCredentialFile(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Try to load configuration
		loadFromFile();
	}

	public void saveToFile() {
		if(getCredentialFile() != null) {
			try {
				ObjectMapper mapper = new ObjectMapper();

				mapper.writeValue(getCredentialFile(), oAuthCredentials);

				getLogger().debug(String.format("Saved %d Credentials using the CredentialManager.", oAuthCredentials.size()));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void loadFromFile() {
		if(getCredentialFile() != null) {
			try {
				ObjectMapper mapper = new ObjectMapper();

				getOAuthCredentials().clear();
				getOAuthCredentials().putAll(mapper.readValue(getCredentialFile(), oAuthCredentials.getClass()));

				getLogger().debug(String.format("Loaded %d Credentials using the CredentialManager.", oAuthCredentials.size()));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
