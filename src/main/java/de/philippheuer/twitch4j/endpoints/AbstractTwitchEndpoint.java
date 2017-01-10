package de.philippheuer.twitch4j.endpoints;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.philippheuer.twitch4j.TwitchClient;
import de.philippheuer.twitch4j.helper.HeaderRequestInterceptor;

import lombok.*;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
public class AbstractTwitchEndpoint {
	
	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractTwitchEndpoint.class);
	
	/**
	 * Holds the API Instance
	 */
	private TwitchClient api;
	
	/**
	 * REST Request Interceptors (adding header-values to requests)
	 */
	private List<ClientHttpRequestInterceptor> restInterceptors = new ArrayList<ClientHttpRequestInterceptor>();
    
	/**
	 * AbstractTwitchEndpoint
	 * @TODO: Description
	 * @param api
	 */
	public AbstractTwitchEndpoint(TwitchClient api) {
		// Properties
		setApi(api);
		
		// Setup Interceptors
		// - Header
		restInterceptors.add(new HeaderRequestInterceptor("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"));
		restInterceptors.add(new HeaderRequestInterceptor("Accept", String.format("application/vnd.twitchtv.v5+json", getApi().getTwitchEndpointVersion())));
		restInterceptors.add(new HeaderRequestInterceptor("Client-ID", getApi().getClientId()));
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
