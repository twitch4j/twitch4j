package me.philippheuer.twitch4j;

import java.io.File;

import me.philippheuer.twitch4j.chat.commands.CommandHandler;
import me.philippheuer.twitch4j.helper.HeaderRequestInterceptor;
import me.philippheuer.twitch4j.helper.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.*;
import me.philippheuer.twitch4j.auth.CredentialManager;
import me.philippheuer.twitch4j.chat.IrcClient;
import me.philippheuer.twitch4j.endpoints.*;
import me.philippheuer.twitch4j.events.EventDispatcher;
import me.philippheuer.twitch4j.pubsub.TwitchPubSub;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import org.springframework.util.Assert;

@Getter
@Setter
public class TwitchClient {

	/**
	 * Event Dispatcher
	 */
	private final EventDispatcher dispatcher = new EventDispatcher(this);

	/**
     * Credential Manager
     */
    private final CredentialManager credentialManager = new CredentialManager();

	/**
	 * Rest Client
	 */
	private RestClient restClient = new RestClient();

	/**
	 * IRC Client
	 */
	private IrcClient ircClient;

	/**
     * PubSub Service
     */
    private TwitchPubSub pubSub;

    /**
     * StreamLabs API
     */
    private StreamlabsClient streamLabsClient;

	/**
	 * Twitch API Endpoint
	 */
	public final String twitchEndpoint = "https://api.twitch.tv/kraken";

	/**
	 * Twitch API Version
	 */
	public final int twitchEndpointVersion = 5;

	/**
	 * Twitch PubSub Endpoint
	 */
	public final String twitchPubSubEndpoint = "wss://pubsub-edge.twitch.tv";

	/**
	 * Twitch IRC Endpoint
	 */
	public final String twitchIrcEndpoint = "irc.chat.twitch.tv:443";

    /**
     * Twitch Client Id
     *  Default Value: Twitch Client Id
     */
	@Singular
    private String clientId = "jzkbprff40iqj646a697cyrvl0zt2m6";

    /**
     * Twitch Client Secret
     */
	@Singular
    private String clientSecret;

	/**
	 * Configuration Directory to save settings
	 */
	@Singular
	private File configurationDirectory;

	/**
	 * Command Handler (CHAT)
	 */
	private CommandHandler commandHandler = new CommandHandler(this);

	/**
     * Constructs a Twitch application instance.
     */
    public TwitchClient(String clientId, String clientSecret) {
        super();

        setClientId(clientId);
        setClientSecret(clientSecret);

        // Provide Instance of TwitchClient to CredentialManager
		credentialManager.setTwitchClient(this);

		// Initialize REST Client
		getRestClient().putRestInterceptor(new HeaderRequestInterceptor("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"));
		getRestClient().putRestInterceptor(new HeaderRequestInterceptor("Accept", String.format("application/vnd.twitchtv.v5+json", getTwitchEndpointVersion())));
		getRestClient().putRestInterceptor(new HeaderRequestInterceptor("Client-ID", getClientId()));
    }

	@Builder(builderMethodName = "builder")
	public static TwitchClient twitchClientBuilder(String clientId, String clientSecret, String configurationDirectory, Boolean configurationAutoSave, StreamlabsClient streamlabsClient) {
		// Reqired Parameters
		Assert.notNull(clientId, "You need to provide a client id!");
		Assert.notNull(clientSecret, "You need to provide a client secret!");

    	// Initalize instance
		final TwitchClient twitchClient = new TwitchClient(clientId, clientSecret);
		twitchClient.getCredentialManager().provideTwitchClient(twitchClient);

		// Optional Parameters
		if(streamlabsClient != null) {
			twitchClient.setStreamLabsClient(streamlabsClient);
			twitchClient.getCredentialManager().provideStreamlabsClient(twitchClient.getStreamLabsClient());
		}

		if(configurationAutoSave != null) {
			twitchClient.getCredentialManager().setSaveCredentials(configurationAutoSave);
		} else {
			twitchClient.getCredentialManager().setSaveCredentials(false);
		}

		if(configurationDirectory != null) {
			twitchClient.setConfigurationDirectory(new File(configurationDirectory));
		}

		// Initialize Managers dependening on the configuration
		twitchClient.getCredentialManager().configurationCreate();

		// Connect to API Endpoints
		twitchClient.connect();

		// Return builded instance
		return twitchClient;
	}

    /**
     * Init Client
     */
    public void connect() {
        // Init IRC
        setIrcClient(new IrcClient(this));

        // Init PubSub API
        setPubSub(new TwitchPubSub(this));
    }

    /**
     * Get Channel Endpoint
     */
    public ChannelEndpoint getChannelEndpoint(Long channelId) {
    	return new ChannelEndpoint(this, channelId);
    }

    /**
     * Get Game Endpoint
     */
    public GameEndpoint getGameEndpoint() {
    	return new GameEndpoint(this);
    }

	/**
	 * Get User Endpoint
	 */
	public StreamEndpoint getStreamEndpoint() {
		return new StreamEndpoint(this);
	}

	/**
	 * Get User Endpoint
	 */
	public UserEndpoint getUserEndpoint() {
		return new UserEndpoint(this);
	}

	/**
	 * Get Community Endpoint
	 */
	public CommunityEndpoint getCommunityEndpoint() {
		return new CommunityEndpoint(this);
	}

	/**
	 * Get Ingest Endpoint
	 */
	public IngestEndpoint getIngestEndpoint() {
		return new IngestEndpoint(this);
	}

	/**
	 * Get Search Endpoint
	 */
	public SearchEndpoint getSearchEndpoint() {
		return new SearchEndpoint(this);
	}

	/**
	 * Get Team Endpoint
	 */
	public TeamEndpoint getTeamEndpoint() {
		return new TeamEndpoint(this);
	}

	/**
	 * Get Video Endpoint
	 */
	public VideoEndpoint getVideoEndpoint() {
		return new VideoEndpoint(this);
	}
}
