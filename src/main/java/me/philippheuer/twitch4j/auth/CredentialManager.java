package me.philippheuer.twitch4j.auth;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.log.Logger;
import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.model.Token;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;

@Getter
@Setter
public class CredentialManager {

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

	private final Map<String, OAuthCredential> oAuthCredentials = new LinkedHashMap<>();

	/**
	 * Holds the credentials
	 */
	private File credentialFile;

	/**
	 * Constructor
	 */
	public CredentialManager() {
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
	 * @param credential Credential Instance
	 */
	public void addTwitchCredential(String key, OAuthCredential credential) {
		// Check Credential Content
		Token token = getTwitchClient().getKrakenEndpoint().getToken(credential);
		credential.setUserId(token.getUserId());
		credential.setUserName(token.getUserName());
		credential.setDisplayName(token.getUserName());
		credential.getOAuthScopes().addAll(token.getAuthorization().getScopes());

		// OAuthCredential Prefix
		addAnyCredential("TWITCH-" + key, credential);
	}

	/**
	 * Adds Streamlabs Credentials to the Credential Manager
	 * @param key Key
	 * @param credential Credential Instance
	 */
	public void addStreamlabsCredential(String key, OAuthCredential credential) {
		// OAuthCredential Prefix
		addAnyCredential("STREAMLABS-" + key, credential);
	}

	/**
	 * Adds any Credentials to the Credential Manager
	 * @param key Key
	 * @param credential Credential Instance
	 */
	private void addAnyCredential(String key, OAuthCredential credential) {
		// Remove value if it exists
		if(getOAuthCredentials().containsKey(key)) {
			Logger.debug(this, "Credentials with key [%s] already present in CredentialManager.", key);
			getOAuthCredentials().remove(key);
		}

		// Add value
		Logger.debug(this, "Added Credentials with key [%s] and data [%s]", key, credential.toString());

		getOAuthCredentials().put(key, credential);

		// Store - if autoSaving was enabled
		if(getSaveCredentials()) {
			saveToFile();
		}
	}

	/**
	 * Get Twitch credentials for channel
	 * @param channelId Channel ID
	 * @return Optional<OAuthCredential> credential with oauth token and access scope.
	 */
	public Optional<OAuthCredential> getTwitchCredentialsForChannel(Long channelId) {
		return getAnyCredential("TWITCH-" + channelId.toString());
	}

	/**
	 * Get Twitch credentials for irc
	 * @return Optional<OAuthCredential> credential with oauth token and access scope.
	 */
	public Optional<OAuthCredential> getTwitchCredentialsForIRC() {
		return getAnyCredential("TWITCH-" + CREDENTIAL_IRC);
	}

	/**
	 * Get Twitch credentials by custom identifier
	 * @return Optional<OAuthCredential> credential with oauth token and access scope.
	 */
	public Optional<OAuthCredential> getTwitchCredentialsForCustomKey(String customKey) {
		return getAnyCredential("TWITCH-" + customKey);
	}

	/**
	 * Get Streamlabs credential by channel
	 * @param channelId Channel ID
	 * @return Optional<OAuthCredential> credential with oauth token and access scope.
	 */
	public Optional<OAuthCredential> getStreamlabsCredentialsForChannel(Long channelId) {
		return getAnyCredential("STREAMLABS-" + channelId.toString());
	}

	/**
	 * Get Streamlabs credential by customKey
	 * @return Optional<OAuthCredential> credential with oauth token and access scope.
	 */
	public Optional<OAuthCredential> getStreamlabsCredentialsForCustomKey(String customKey) {
		return getAnyCredential("STREAMLABS-" + customKey);
	}

	/**
	 * Private Method to get a credential by key
	 * @param key The key to search for.
	 * @return Optional<OAuthCredential>
	 */
	private Optional<OAuthCredential> getAnyCredential(String key) {
		if(getOAuthCredentials().containsKey(key)) {
			return Optional.ofNullable((OAuthCredential) getOAuthCredentials().get(key));
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

				Logger.debug(this, "Saved %d Credentials using the CredentialManager.", oAuthCredentials.size());
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

				Map<String, OAuthCredential> loadedCredentials = mapper.readValue(getCredentialFile(), new TypeReference<LinkedHashMap<String, OAuthCredential>>(){});
				getOAuthCredentials().putAll(loadedCredentials);

				Logger.debug(this, "Loaded %d Credentials using the CredentialManager.", oAuthCredentials.size());

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
