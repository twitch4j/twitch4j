package de.philippheuer.twitch4j.endpoints;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import de.philippheuer.twitch4j.Twitch4J;
import de.philippheuer.twitch4j.model.*;

import lombok.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @requires_scope channel_read
 */
@Data
public class GetChannelById extends AbstractTwitchEndpoint {
	
	/**
	 * Get
	 */
	public GetChannelById(Twitch4J api, String channelId) {
		super(api);
		
		System.out.println("Preparing Request");
		
		String requestUrl = String.format("%s/channels/%s", getApi().getTwitchEndpoint(), channelId);
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(getRestInterceptors());
    	Channel responseObject = restTemplate.getForObject(requestUrl, Channel.class);
    	
    	getLogger().info("SpringRest: " + responseObject.toString());
	}
}
