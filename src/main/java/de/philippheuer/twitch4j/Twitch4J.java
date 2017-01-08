package de.philippheuer.twitch4j;

import de.philippheuer.twitch4j.helper.HttpClient;
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
	 * HTTP Client
	 */
	protected final HttpClient httpClient = new HttpClient();
	
    /**
     * Twitch Client Id
     */
    private String clientId;
    
    /**
     * Twitch Client Id
     */
    private String clientSecret;
    
    /**
     * Constructs a Twitch application instance.
     */
    public Twitch4J(String clientId, String clientSecret) {
        super();
        
        setClientId(clientId);
        setClientSecret(clientSecret);
    }
    
    /**
     * Endpoint: GetUserIdFromUserName
     */
    public String getUserIdFromUserName(String userName) {
    	
    	return "";
    }
}
