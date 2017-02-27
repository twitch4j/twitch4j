package me.philippheuer.util.rest;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RestClient {

	/**
	 * REST Request Interceptors (adding header-values to requests)
	 */
	private List<ClientHttpRequestInterceptor> restInterceptors;

	/**
	 * Constructor
	 */
	public RestClient() {
		super();

		setRestInterceptors(new ArrayList<ClientHttpRequestInterceptor>());
	}

	public void putRestInterceptor(ClientHttpRequestInterceptor interceptor) {
		restInterceptors.add(interceptor);
	}

	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		// Request Interceptors
		restTemplate.setInterceptors(new ArrayList<ClientHttpRequestInterceptor>());
		restTemplate.getInterceptors().addAll(getRestInterceptors());

		// Default Error Handler
		restTemplate.setErrorHandler(new RestErrorHandler());

		return restTemplate;
	}

	/**
	 * @param OAuthCredential
	 * @return
	 */
	public RestTemplate getPrivilegedRestTemplate(OAuthCredential OAuthCredential) {
		// Get Rest Template
		RestTemplate restTemplate = getRestTemplate();

		// Request Interceptors (add Authorization)
		restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", OAuthCredential.getOAuthToken())));

		return restTemplate;
	}

	public RestTemplate getPlainRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new RestErrorHandler());

		return restTemplate;
	}
}
