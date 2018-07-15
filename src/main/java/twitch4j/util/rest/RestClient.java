package twitch4j.util.rest;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.util.DefaultUriBuilderFactory;
import twitch4j.auth.model.OAuthCredential;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

/**
 * Rest Client Wrapper
 *
 * @author Philipp Heuer
 * @version %I%, %G%
 * @since 1.0
 */

@Getter
@NoArgsConstructor
public class RestClient {

	/**
	 * REST Request Interceptors (adding header-values/query parameters/... to requests)
	 */
	private final List<ClientHttpRequestInterceptor> restInterceptors = new ArrayList<ClientHttpRequestInterceptor>();

	/**
	 * Adds a interceptor to the Rest Template.
	 *
	 * @param interceptor Interceptor, that will be executed prior to the rest request.
	 * @see HeaderRequestInterceptor
	 * @see QueryRequestInterceptor
	 */
	public void putRestInterceptor(ClientHttpRequestInterceptor interceptor) {
		restInterceptors.add(interceptor);
	}

	/**
	 * Gets a Rest Template.
	 *
	 * @return A RestTemplate for rest requests.
	 */
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = getPlainRestTemplate();

		putRestInterceptor(new LoggingRequestInterceptor());

		restTemplate.setInterceptors(restInterceptors);
		restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("https://api.twitch.tv/kraken"));

		return restTemplate;
	}

	/**
	 * Gets a Rest Template with authorization.
	 *
	 * @param OAuthCredential Credential, to add authentication headers to the rest template.
	 * @return A RestTemplate for rest requests.
	 */
	public RestTemplate getPrivilegedRestTemplate(OAuthCredential OAuthCredential) {
		// Get Rest Template
		RestTemplate restTemplate = getRestTemplate();

		// Request Interceptors (add Authorization)
		restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", OAuthCredential.getToken())));

		return restTemplate;
	}

	/**
	 * Gets a Rest Template without any interceptors.
	 *
	 * @return A RestTemplate for rest requests.
	 */
	public RestTemplate getPlainRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new RestErrorHandler());

		return restTemplate;
	}
}
