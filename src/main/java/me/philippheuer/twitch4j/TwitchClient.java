package me.philippheuer.twitch4j;

import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import me.philippheuer.twitch4j.auth.CredentialManager;
import me.philippheuer.twitch4j.endpoints.*;
import me.philippheuer.twitch4j.events.EventDispatcher;
import me.philippheuer.twitch4j.message.MessageInterface;
import me.philippheuer.twitch4j.message.commands.CommandHandler;
import me.philippheuer.twitch4j.message.irc.listener.IRCEventListener;
import me.philippheuer.twitch4j.modules.ModuleLoader;
import me.philippheuer.util.rest.HeaderRequestInterceptor;
import me.philippheuer.util.rest.RestClient;

import java.io.File;

/**
 * TwitchClient is the core class for all api operations.
 * <p>
 * The TwitchClient class is the central component, that grants access
 * to the various rest endpoints, the twitch chat interface and the
 * client related services. (CredentialManager/CommandHandler/...)
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@Setter
public class TwitchClient {

	/**
	 * Service to dispatch Events
	 */
	private final EventDispatcher dispatcher = new EventDispatcher(this);

	/**
	 * Services to store/request credentials
	 */
	private final CredentialManager credentialManager = new CredentialManager();

	/**
	 * RestClient to build the rest requests
	 */
	private final RestClient restClient = new RestClient();

	/**
	 * Twitch IRC Client
	 */
	private final MessageInterface messageInterface = new MessageInterface(this);

	/**
	 * Twitch API Version
	 */
	public final int twitchEndpointVersion = 5;

	/**
	 * Twitch Application - Client Id
	 * Default Value: Twitch Client Id
	 */
	@Singular
	private String clientId = "jzkbprff40iqj646a697cyrvl0zt2m6";

	/**
	 * Twitch Application - Client Secret
	 */
	@Singular
	private String clientSecret;

	/**
	 * Configuration Directory to save settings
	 */
	@Singular
	private File configurationDirectory;

	/**
	 * Command Handler (CHAT Commands and Features)
	 */
	private CommandHandler commandHandler = new CommandHandler(this);

	/**
	 * NEW Feature: Modules.
	 */
	private final ModuleLoader moduleLoader = new ModuleLoader(this);

	/**
	 * Class Constructor - Creates a new TwitchClient Instance for the provided app.
	 * <p>
	 * This will also initialize the rest interceptors, that provide oauth tokens/get/post parameters
	 * on the fly to easily build the rest requests.
	 *
	 * @param clientId     Twitch Application - Id
	 * @param clientSecret Twitch Application - Secret
	 */
	public TwitchClient(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;

		// Provide Instance of TwitchClient to CredentialManager
		credentialManager.setTwitchClient(this);

		// EventSubscribers
		// - Commands
		dispatcher.registerListener(getCommandHandler());
		// - IRC Event Listeners
		dispatcher.registerListener(new IRCEventListener(this));
		// Initialize REST Client
		restClient.putRestInterceptor(new HeaderRequestInterceptor("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"));
		restClient.putRestInterceptor(new HeaderRequestInterceptor("Accept", "application/vnd.twitchtv.v5+json"));
		restClient.putRestInterceptor(new HeaderRequestInterceptor("Client-ID", getClientId()));
	}

	/**
	 * Connect to other related services.
	 * <p>
	 * This methods opens the connection to the twitch irc server and the pubsub endpoint.
	 * Connect needs to be called after initalizing the {@link CredentialManager}.
	 */
	public void connect() {
		getMessageInterface().connect();
	}

	/**
	 * Disconnect from other related services.
	 * <p>
	 * This methods closes the connection to the twitch irc server and the pubsub endpoint.
	 */
	public void disconnect() {
		getMessageInterface().disconnect();
	}

	/**
	 * Reconnect to other related services.
	 * <p>
	 * This methods reconnects to the twitch irc server and the pubsub endpoint.
	 */
	public void reconnect() {
		getMessageInterface().reconnect();
	}

	/**
	 * Returns an a new KrakenEndpoint instance.
	 * <p>
	 * The Kraken Endpoint is the root of the twitch api.
	 * Querying the Kraken Endpoint gives information about the currently used token.
	 *
	 * @return a new instance of {@link KrakenEndpoint}
	 */
	public KrakenEndpoint getKrakenEndpoint() {
		return new KrakenEndpoint(this);
	}

	/**
	 * Returns an a new ChannelFeedEndpoint instance.
	 *
	 * @return a new instance of {@link ChannelFeedEndpoint}
	 */
	public ChannelFeedEndpoint getChannelFeedEndpoint() {
		return new ChannelFeedEndpoint(this);
	}

	/**
	 * Returns an a new ChannelEndpoint instance - identifying the channel by the channel id.
	 * <p>
	 * The Channel Endpoint instances allow you the query or set data for a specific channel,
	 * therefore you need to provide information to identify a unique channel.
	 *
	 * @param channelId ID of the twitch channel
	 * @return a new instance of {@link ChannelEndpoint}
	 * @see me.philippheuer.twitch4j.model.Channel
	 */
	public ChannelEndpoint getChannelEndpoint(Long channelId) {
		return new ChannelEndpoint(this, channelId);
	}

	/**
	 * Returns an a new ChannelEndpoint instance - identifying the channel by the channel name.
	 * <p>
	 * The Channel Endpoint instances allow you the query or set data for a specific channel,
	 * therefore you need to provide information to identify a unique channel.
	 *
	 * @param channelName Name of the twitch channel
	 * @return a new instance of {@link ChannelEndpoint}
	 * @see me.philippheuer.twitch4j.model.Channel
	 */
	public ChannelEndpoint getChannelEndpoint(String channelName) {
		return new ChannelEndpoint(this, channelName);
	}

	/**
	 * Returns an a new GameEndpoint instance.
	 * <p>
	 * The Game Endpoint instance allows you to access information about the all available games on twitch.
	 *
	 * @return a new instance of {@link GameEndpoint}
	 * @see me.philippheuer.twitch4j.model.Game
	 */
	public GameEndpoint getGameEndpoint() {
		return new GameEndpoint(this);
	}

	/**
	 * Returns an a new StreamEndpoint instance.
	 * <p>
	 * The Stream Endpoint provides information about all current live streams and related metadata.
	 * For more information about the data, check out the {@link me.philippheuer.twitch4j.model.Stream} model.
	 *
	 * @return a new instance of {@link StreamEndpoint}
	 * @see me.philippheuer.twitch4j.model.Stream
	 */
	public StreamEndpoint getStreamEndpoint() {
		return new StreamEndpoint(this);
	}

	/**
	 * Returns an a new UserEndpoint instance.
	 * <p>
	 * The User Endpoint provides access to user-related informations and actions.
	 * For more information about the available methods, check out the {@link UserEndpoint}.
	 *
	 * @return a new instance of {@link UserEndpoint}
	 * @see me.philippheuer.twitch4j.model.User
	 */
	public UserEndpoint getUserEndpoint() {
		return new UserEndpoint(this);
	}

	/**
	 * Returns an a new CommunityEndpoint instance.
	 * <p>
	 * The Community Endpoint allows you to fetch information or manage your communities using the api.
	 * The community methods usually return a {@link me.philippheuer.twitch4j.model.Community} model.
	 *
	 * @return a new instance of {@link CommunityEndpoint}
	 * @see me.philippheuer.twitch4j.model.Community
	 */
	public CommunityEndpoint getCommunityEndpoint() {
		return new CommunityEndpoint(this);
	}

	/**
	 * Returns an a new IngestEndpoint instance.
	 * <p>
	 * The Ingest Endpoint allows you to fetch a list of the twitch ingest servers.
	 *
	 * @return a new instance of {@link IngestEndpoint}
	 * @see me.philippheuer.twitch4j.model.Ingest
	 */
	public IngestEndpoint getIngestEndpoint() {
		return new IngestEndpoint(this);
	}

	/**
	 * Returns an a new SearchEndpoint instance.
	 * <p>
	 * The Search Endpoint allows you to search for {@link me.philippheuer.twitch4j.model.Channel}s,
	 * {@link me.philippheuer.twitch4j.model.Game}s or {@link me.philippheuer.twitch4j.model.Stream}s.
	 *
	 * @return a new instance of {@link SearchEndpoint}
	 * @see me.philippheuer.twitch4j.model.Stream
	 * @see me.philippheuer.twitch4j.model.Game
	 * @see me.philippheuer.twitch4j.model.Channel
	 */
	public SearchEndpoint getSearchEndpoint() {
		return new SearchEndpoint(this);
	}

	/**
	 * Returns an a new TeamEndpoint instance.
	 * <p>
	 * The Team Endpoint provides a list of all teams and detailed information about single teams.
	 *
	 * @return a new instance of {@link TeamEndpoint}
	 * @see me.philippheuer.twitch4j.model.Team
	 */
	public TeamEndpoint getTeamEndpoint() {
		return new TeamEndpoint(this);
	}

	/**
	 * Returns an a new VideoEndpoint instance.
	 * <p>
	 * The Video Endpoint provides access to videos that twitch users recoded.
	 *
	 * @return a new instance of {@link VideoEndpoint}
	 * @see me.philippheuer.twitch4j.model.Video
	 */
	public VideoEndpoint getVideoEndpoint() {
		return new VideoEndpoint(this);
	}

	/**
	 * Returns an a new TMIEndpoint instance.
	 * <p>
	 * The Twitch Messaging Service (TMI) is the chat service used in twitch.
	 * This is an unofficial api and can break at any point without any notice.
	 *
	 * @return a new instance of {@link TMIEndpoint}
	 */
	public TMIEndpoint getTMIEndpoint() {
		return new TMIEndpoint(this);
	}

	/**
	 * Returns an a new UnofficialEndpoint instance.
	 *
	 * @return a new instance of {@link UnofficialEndpoint}
	 */
	public UnofficialEndpoint getUnofficialEndpoint() {
		return new UnofficialEndpoint(this);
	}

	/**
	 * Returns a new ChatEndpoint instance.
	 *
	 * @return a new instance of {@link ChatEndpoint}
	 */
	public ChatEndpoint getChatEndpoint() {
		return new ChatEndpoint(this);
	}
}
