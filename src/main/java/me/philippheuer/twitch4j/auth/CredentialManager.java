package me.philippheuer.twitch4j.auth;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.streamlabs.StreamlabsCredential;
import me.philippheuer.twitch4j.auth.model.twitch.TwitchCredential;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
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
	 * Holds the Twitch Instance
	 */
	private TwitchClient twitchClient;

	/**
	 * Holds the Twitch Instance
	 */
	private StreamlabsClient streamlabsClient;

	/**
	 * OAuth Response Listener
	 */
	private OAuthHandler oAuthHandler;

	/**
	 * OAuth - Twitch
	 */
	private OAuthTwitch oAuthTwitch;

	/**
	 * OAuth - Streamlabs
	 */
	private OAuthStreamlabs oAuthStreamlabs;

	/**
	 * Save all credentials locally be default?
	 */
	private Boolean saveCredentials = false;

	// IRC Key
	public static String CREDENTIAL_IRC = "IRC";

	private final Map<String, Object> oAuthCredentials = new LinkedHashMap<>();

	/**
	 * Holds the credentials
	 */
	private File credentialFile;

	/**
	 * Constructor
	 */
	public CredentialManager(TwitchClient twitchClient) {
		super();
		setOAuthHandler(new OAuthHandler(this));
	}

	public void provideTwitchClient(TwitchClient twitchClient) {
		setTwitchClient(twitchClient);
		setOAuthTwitch(new OAuthTwitch(this));
	}

	public void provideStreamlabsClient(StreamlabsClient streamlabsClient) {
		setStreamlabsClient(streamlabsClient);
		setOAuthStreamlabs(new OAuthStreamlabs(this));
	}

	/**
	 * Adds Twitch Credentials to the Credential Manager
	 * @param key Key
	 * @param twitchCredential Credential Instance
	 */
	public void addCredential(String key, TwitchCredential twitchCredential) {
		// TwitchCredential Prefix
		key = "TWITCH-" + key;

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
	 * Adds Streamlabs Credentials to the Credential Manager
	 * @param key Key
	 * @param credential Credential Instance
	 */
	public void addCredential(String key, StreamlabsCredential credential) {
		// TwitchCredential Prefix
		key = "STREAMLABS-" + key;

		// Remove value if it exists
		if(getOAuthCredentials().containsKey(key)) {
			getLogger().debug(String.format("Credentials with key [%s] already present in CredentialManager.", key));
			getOAuthCredentials().remove(key);
		}

		// Add value
		getLogger().debug(String.format("Added Credentials with key [%s] and data [%s]", key, credential.toString()));

		getOAuthCredentials().put(key, credential);

		// Store
		saveToFile();
	}

	/**
	 * Get Twitch credentials for channel
	 * @param channel Channel
	 * @return Optional<TwitchCredential> credential with oauth token and access scope.
	 */
	public Optional<TwitchCredential> getTwitchCredentialsForChannel(Channel channel) {
		if(getOAuthCredentials().containsKey("TWITCH-" + channel.getId().toString())) {
			return Optional.ofNullable((TwitchCredential) getOAuthCredentials().get("TWITCH-" + channel.getId().toString()));
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Get Twitch credentials for irc
	 * @return Optional<TwitchCredential> credential with oauth token and access scope.
	 */
	public Optional<TwitchCredential> getTwitchCredentialsForIRC() {
		if(getOAuthCredentials().containsKey("TWITCH-" + CREDENTIAL_IRC)) {
			return Optional.ofNullable((TwitchCredential) getOAuthCredentials().get("TWITCH-" + CREDENTIAL_IRC));
		} else {
			return Optional.empty();
		}
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

				Map<String, TwitchCredential> loadedCredentials = mapper.readValue(getCredentialFile(), new TypeReference<LinkedHashMap<String, TwitchCredential>>(){});
				getOAuthCredentials().putAll(loadedCredentials);

				getLogger().debug(String.format("Loaded %d Credentials using the CredentialManager.", oAuthCredentials.size()));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
