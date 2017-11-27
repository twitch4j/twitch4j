package me.philippheuer.twitch4j.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.auth.model.OAuthRequest;
import me.philippheuer.twitch4j.events.event.system.AuthTokenExpiredEvent;
import me.philippheuer.twitch4j.model.Token;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import java.io.File;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * This class managed all oauth credentials for the verious services.
 * <p>
 * This class contains methods to request, load, store and get oauth
 * credentials for a specific channel.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@Setter
public class CredentialManager {

	/**
	 * Static key for the primary irc credential. This account will be used as sender for all chat messages.
	 */
	public static String CREDENTIAL_IRC = "IRC";

	/**
	 * Holds the Twitch Instance
	 */
	private TwitchClient twitchClient;

	/**
	 * OAuth Response Listener
	 */
	private OAuthHandler oAuthHandler;

	/**
	 * OAuth - Twitch
	 */
	private OAuthTwitch oAuthTwitch;

	/**
	 * A map that stores all current requests.
	 * <p>
	 * This map temporarily stores all current ongoing requests. This is required to link
	 * the oauth states to the requests.
	 */
	protected ExpiringMap<String, OAuthRequest> oAuthRequestCache = ExpiringMap.builder()
			.expiration(3600, TimeUnit.SECONDS)
			.expirationPolicy(ExpirationPolicy.CREATED)
			.variableExpiration()
			.build();

	/**
	 * Save all credentials locally be default?
	 */
	private Boolean saveCredentials = false;

	/**
	 * A key-value map that contains all currently avaiable credentials.
	 */
	private final Map<String, OAuthCredential> oAuthCredentials = new LinkedHashMap<>();

	/**
	 * Holds the credentials
	 */
	private File credentialFile;

	/**
	 * Class Constructor
	 */
	public CredentialManager() {
		super();
		setOAuthHandler(new OAuthHandler(this));

		// Define Token Refresh Task
		Thread tokenRefreshTask = new Thread(new Runnable() {
			public void run() {
				// Keep running
				while(true) {
					try {
						// For each Credential
						for(OAuthCredential credential : getOAuthCredentials().values()) {
							// Check for expiry date
							if(credential.getTokenExpiresAt() == null) {
								continue;
							}

							// Is the token expired?
							if(credential.getTokenExpiresAt().before(Calendar.getInstance())) {
								// Dispatch token expired event
								AuthTokenExpiredEvent event = new AuthTokenExpiredEvent(credential);
								getTwitchClient().getDispatcher().dispatch(event);

								Logger.info(this, "Token Expired, triggering Refresh Event! [%s]", event.toString());
							}
						}

						// Delay
						Thread.sleep(10 * 1000);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		tokenRefreshTask.start();
	}

	/**
	 * Provides the CredentialManager with the Twitch Context.
	 *
	 * @param twitchClient The TwitchClient.
	 */
	public void provideTwitchClient(TwitchClient twitchClient) {
		setTwitchClient(twitchClient);
		setOAuthTwitch(new OAuthTwitch(this));

		// Register OAuthTwitch to get Events
		getTwitchClient().getDispatcher().registerListener(getOAuthTwitch());
	}

	/**
	 * Adds Twitch Credentials to the Credential Manager
	 *
	 * @param key        Key
	 * @param credential Credential Instance
	 */
	public void addTwitchCredential(String key, OAuthCredential credential) {
		// Check Credential Content
		Token token = getTwitchClient().getKrakenEndpoint().getToken(credential);
		credential.setType("twitch");
		credential.setUserId(token.getUserId());
		credential.setUserName(token.getUserName());
		credential.setDisplayName(token.getUserName());
		if(token.getAuthorization() != null)  {
			credential.getOAuthScopes().addAll(token.getAuthorization().getScopes());
		}

		// OAuthCredential Prefix
		addAnyCredential("TWITCH-" + key, credential);
	}

	/**
	 * Adds Streamlabs Credentials to the Credential Manager
	 *
	 * @param key        Key
	 * @param credential Credential Instance
	 */
	public void addStreamlabsCredential(String key, OAuthCredential credential) {
		// OAuthCredential Prefix
		credential.setType("streamlabs");
		addAnyCredential("STREAMLABS-" + key, credential);
	}

	/**
	 * Adds any Credentials to the Credential Manager
	 *
	 * @param key        Key
	 * @param credential Credential Instance
	 */
	private void addAnyCredential(String key, OAuthCredential credential) {
		// Remove value if it exists
		if (getOAuthCredentials().containsKey(key)) {
			Logger.debug(this, "Credentials with key [%s] already present in CredentialManager.", key);
			getOAuthCredentials().remove(key);
		}

		// Add value
		Logger.debug(this, "Added Credentials with key [%s] and data [%s]", key, credential.toString());

		getOAuthCredentials().put(key, credential);

		// Store - if autoSaving was enabled
		if (getSaveCredentials()) {
			saveToFile();
		}
	}

	/**
	 * Get Twitch credentials for channel
	 *
	 * @param channelId Channel ID
	 * @return Optional OAuthCredential credential with oauth token and access scope.
	 */
	public Optional<OAuthCredential> getTwitchCredentialsForChannel(Long channelId) {
		return getAnyCredential("TWITCH-" + channelId.toString());
	}

	/**
	 * Get Twitch credentials for irc
	 *
	 * @return Optional OAuthCredential credential with oauth token and access scope.
	 */
	public Optional<OAuthCredential> getTwitchCredentialsForIRC() {
		return getAnyCredential("TWITCH-" + CREDENTIAL_IRC);
	}

	/**
	 * Get Twitch credentials by custom identifier
	 *
	 * @param customKey The key, the credential was stored with.
	 * @return Optional OAuthCredential credential with oauth token and access scope.
	 */
	public Optional<OAuthCredential> getTwitchCredentialsForCustomKey(String customKey) {
		return getAnyCredential("TWITCH-" + customKey);
	}

	/**
	 * Get Streamlabs credential by channel
	 *
	 * @param channelId Channel ID
	 * @return Optional OAuthCredential credential with oauth token and access scope.
	 */
	public Optional<OAuthCredential> getStreamlabsCredentialsForChannel(Long channelId) {
		return getAnyCredential("STREAMLABS-" + channelId.toString());
	}

	/**
	 * Get Streamlabs credential by customKey
	 *
	 * @param customKey The key, the credential was stored with.
	 * @return Optional OAuthCredential credential with oauth token and access scope.
	 */
	public Optional<OAuthCredential> getStreamlabsCredentialsForCustomKey(String customKey) {
		return getAnyCredential("STREAMLABS-" + customKey);
	}

	/**
	 * Private Method to get a credential by key
	 *
	 * @param key The key to search for.
	 * @return Optional<OAuthCredential>
	 */
	private Optional<OAuthCredential> getAnyCredential(String key) {
		if (getOAuthCredentials().containsKey(key)) {
			return Optional.ofNullable((OAuthCredential) getOAuthCredentials().get(key));
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Should be called, when a credential is externally modified.
	 */
	public void onCredentialChanged() {
		saveToFile();
	}

	/**
	 * Initializes the Configuration (creates the files)
	 */
	public void initializeConfiguration() {
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

	private void saveToFile() {
		if (getCredentialFile() != null) {
			try {
				ObjectMapper mapper = new ObjectMapper();

				mapper.writeValue(getCredentialFile(), oAuthCredentials);

				Logger.debug(this, "Saved %d Credentials using the CredentialManager.", oAuthCredentials.size());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void loadFromFile() {
		if (getCredentialFile() != null) {
			// Clear present credentials
			getOAuthCredentials().clear();

			// Load credentials from file
			try {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, OAuthCredential> loadedCredentials = mapper.readValue(getCredentialFile(), new TypeReference<LinkedHashMap<String, OAuthCredential>>() {
				});
				getOAuthCredentials().putAll(loadedCredentials);

				Logger.debug(this, "Loaded %d Credentials using the CredentialManager.", oAuthCredentials.size());

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
