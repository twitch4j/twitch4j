package de.philippheuer.twitch4j;

import de.philippheuer.twitch4j.endpoints.*;
import de.philippheuer.twitch4j.pubsub.TwitchPubSub;
import lombok.*;

@Getter
@Setter
public class Twitch4J {
	
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
     * Twitch Client Id
     */
    private String clientId;
    
    /**
     * Twitch Client Id
     */
    private String clientSecret;
    
    /**
     * PubSub Service
     */
    private TwitchPubSub pubSub;
    
    /**
     * Constructs a Twitch application instance.
     */
    public Twitch4J(String clientId, String clientSecret) {
        super();
        
        setClientId(clientId);
        setClientSecret(clientSecret);
        
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
