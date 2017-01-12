package de.philippheuer.twitch4j;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.philippheuer.twitch4j.auth.CredentialManager;
import de.philippheuer.twitch4j.chat.IrcClient;
import de.philippheuer.twitch4j.endpoints.*;
import de.philippheuer.twitch4j.events.EventDispatcher;
import de.philippheuer.twitch4j.pubsub.TwitchPubSub;
import lombok.*;

@Getter
@Setter
public class TwitchClient {
	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(TwitchClient.class);
	
	/**
	 * Event Dispatcher
	 */
	private final EventDispatcher dispatcher = new EventDispatcher(this);
	
	/**
     * Credential Manager
     */
    private final CredentialManager credentialManager = new CredentialManager();
	
    /**
	 * IRC Client
	 */
	private IrcClient ircClient;
	
	/**
     * PubSub Service
     */
    private TwitchPubSub pubSub;
    
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
     *  Default Value: Twitch
     */
    private String clientId = "jzkbprff40iqj646a697cyrvl0zt2m6";
    
    /**
     * Twitch Client Secret
     */
    private String clientSecret;
    
    /**
     * Constructs a Twitch application instance.
     */
    public TwitchClient(String clientId, String clientSecret) {
        super();
        
        setClientId(clientId);
        setClientSecret(clientSecret);
    }
    
    /**
     * Constructs a Twitch application instance using a configuration file.
     */
    public TwitchClient(File file) {
    	super();
        
        // TODO Load Configuration ApacheConfiguration2
    }
    
    
    /**
     * Init Client
     */
    public void connect() {
        // Connect to IRC
        setIrcClient(new IrcClient(this));
        
        // Init PubSub API
        setPubSub(new TwitchPubSub(this));
    }
    
    /**
     * Get User Endpoint
     */
    public UserEndpoint getUserEndpoint() {
    	return new UserEndpoint(this);
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
}
