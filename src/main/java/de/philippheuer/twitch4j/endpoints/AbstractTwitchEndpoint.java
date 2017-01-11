package de.philippheuer.twitch4j.endpoints;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.philippheuer.twitch4j.TwitchClient;
import de.philippheuer.twitch4j.helper.HeaderRequestInterceptor;

import lombok.*;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
public class AbstractTwitchEndpoint {
	
	/**
	 * Cache - Objects
	 */
	static protected Map<String, Object> restObjectCache = ExpiringMap.builder()
			.expiration(1, TimeUnit.MINUTES)
			.expirationPolicy(ExpirationPolicy.ACCESSED)
			.build();
	
	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractTwitchEndpoint.class);
	
	/**
	 * Holds the API Instance
	 */
	private TwitchClient client;
	
	/**
	 * REST Request Interceptors (adding header-values to requests)
	 */
	private List<ClientHttpRequestInterceptor> restInterceptors = new ArrayList<ClientHttpRequestInterceptor>();
    
	/**
	 * AbstractTwitchEndpoint
	 * @TODO: Description
	 * @param api
	 */
	public AbstractTwitchEndpoint(TwitchClient client) {
		// Properties
		setClient(client);
		
		// Setup Interceptors
		// - Header
		restInterceptors.add(new HeaderRequestInterceptor("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"));
		restInterceptors.add(new HeaderRequestInterceptor("Accept", String.format("application/vnd.twitchtv.v5+json", getClient().getTwitchEndpointVersion())));
		restInterceptors.add(new HeaderRequestInterceptor("Client-ID", getClient().getClientId()));
		//restInterceptors.add(new HeaderRequestInterceptor("Authorization", "OAuth cfabdegwdoklmawdzdo98xt2fo512y"));
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(getRestInterceptors());
		// restTemplate.setErrorHandler(errorHandler);
		
		return restTemplate;
	}
}
