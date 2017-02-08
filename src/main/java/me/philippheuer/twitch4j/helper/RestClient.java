package me.philippheuer.twitch4j.helper;

import lombok.*;

import me.philippheuer.twitch4j.auth.model.OAuthCredential;
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
	private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

	/**
	 * REST Request Interceptors (adding header-values to requests)
	 */
	private List<ClientHttpRequestInterceptor> restInterceptors = new ArrayList<ClientHttpRequestInterceptor>();

	/**
	 * Constructor
	 */
	public RestClient() {
		super();
	}

	public void putRestInterceptor(ClientHttpRequestInterceptor interceptor) {
		restInterceptors.add(interceptor);
	}

	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(new ArrayList<ClientHttpRequestInterceptor>());
		restTemplate.getInterceptors().addAll(getRestInterceptors());
		restTemplate.setErrorHandler(new RestErrorHandler());

		return restTemplate;
	}

	/**
	 *
	 * @param OAuthCredential
	 * @return
	 */
	public RestTemplate getPrivilegedRestTemplate(OAuthCredential OAuthCredential) {
		List<ClientHttpRequestInterceptor> localRestInterceptors = new ArrayList<ClientHttpRequestInterceptor>();
		localRestInterceptors.addAll(getRestInterceptors());
		localRestInterceptors.add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", OAuthCredential.getOAuthToken())));

		RestTemplate restTemplate = getRestTemplate();
		restTemplate.setInterceptors(localRestInterceptors);

		return restTemplate;
	}

	public RestTemplate getPlainRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new RestErrorHandler());

		return restTemplate;
	}
}
