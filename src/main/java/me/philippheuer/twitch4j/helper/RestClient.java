package me.philippheuer.twitch4j.helper;

import lombok.*;

import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.twitch.model.TwitchCredential;
import me.philippheuer.twitch4j.endpoints.AbstractTwitchEndpoint;
import me.philippheuer.twitch4j.helper.HeaderRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RestClient {
	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractTwitchEndpoint.class);

	/**
	 * REST Request Interceptors (adding header-values to requests)
	 */
	private List<ClientHttpRequestInterceptor> restInterceptors = new ArrayList<ClientHttpRequestInterceptor>();

	/**
	 *
	 */
	public RestClient() {
		super();
	}

	public void putRestInterceptor(ClientHttpRequestInterceptor interceptor) {
		restInterceptors.add(interceptor);
	}

	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(getRestInterceptors());
		// restTemplate.setErrorHandler(errorHandler);

		return restTemplate;
	}

	/**
	 *
	 * @param twitchCredential
	 * @return
	 */
	public RestTemplate getPrivilegedRestTemplate(TwitchCredential twitchCredential) {
		List<ClientHttpRequestInterceptor> localRestInterceptors = new ArrayList<ClientHttpRequestInterceptor>();
		localRestInterceptors.addAll(getRestInterceptors());
		localRestInterceptors.add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", twitchCredential.getOAuthToken())));

		RestTemplate restTemplate = getRestTemplate();
		restTemplate.setInterceptors(localRestInterceptors);

		return restTemplate;
	}

	public RestTemplate getPlainRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		return restTemplate;
	}
}
