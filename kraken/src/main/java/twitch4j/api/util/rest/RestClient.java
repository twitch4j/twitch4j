package twitch4j.api.util.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Rest Client Wrapper
 *
 * @author Philipp Heuer
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public class RestClient {

	/**
	 * Base URL for {@link RestTemplate}
	 */
	private final String baseUrl;

	/**
	 * REST Request Interceptors (adding header-values/query parameters/... to requests)
	 */
	private final List<ClientHttpRequestInterceptor> restInterceptors = new ArrayList<>();

	/**
	 * Adds a interceptor to the Rest Template.
	 *
	 * @param interceptor Interceptor, that will be executed prior to the http request.
	 * @see HeaderRequestInterceptor
	 */
	public RestClient addInterceptor(ClientHttpRequestInterceptor interceptor) {
		restInterceptors.add(interceptor);
		return this;
	}

	/**
	 * Gets a Rest Template.
	 *
	 * @return A RestTemplate for http requests.
	 */
	public RestTemplate getRestTemplate(ObjectMapper mapper) {
		RestTemplate restTemplate = getPlainRestTemplate(mapper);

		// Request Interceptors
		restTemplate.setInterceptors(restInterceptors);

		return restTemplate;
	}

	/**
	 * Gets a Rest Template without any interceptors.
	 *
	 * @return A RestTemplate for http requests.
	 */
	public RestTemplate getPlainRestTemplate(ObjectMapper mapper) {
		RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
		restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
		restTemplate.setErrorHandler(new RestErrorHandler());
		restTemplate.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter(mapper)));

		return restTemplate;
	}
}
