package me.philippheuer.util.rest;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.support.HttpRequestDecorator;

import java.io.IOException;

/**
 * Spring Rest: Query Request Interceptor
 * <p>
 * The query request interceptors can be applied to a {@link org.springframework.web.client.RestTemplate} and
 * will add query parameters at runtime.
 *
 * @author Philipp Heuer
 * @version %I%, %G%
 * @since 1.0
 */
public class QueryRequestInterceptor implements ClientHttpRequestInterceptor {

	/**
	 * Name of the query parameter.
	 */
	private final String name;

	/**
	 * Value of the query parameter.
	 */
	private final String value;

	/**
	 * Class Constructor
	 *
	 * @param name Name of the query parameter to add.
	 * @param value Value of the query parameter to add.
	 */
	public QueryRequestInterceptor(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpRequestDecorator httpRequest = new HttpRequestDecorator(request);

		if(name != null && value != null) {
			httpRequest.addParameter(name, value);
		}

		return execution.execute(httpRequest, body);
	}
}
