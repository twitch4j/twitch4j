package me.philippheuer.util.rest;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

/**
 * Spring Rest: Header Request Interceptor
 * <p>
 * The header request interceptors can be applied to a {@link org.springframework.web.client.RestTemplate} and
 * will add the header's at runtime.
 *
 * @author Philipp Heuer
 * @version %I%, %G%
 * @since 1.0
 */
public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

	/**
	 * Header Key
	 */
	private final String name;

	/**
	 * Header Value
	 */
	private final String value;

	/**
	 * Class Constructor
	 *
	 * @param name Name of the header key to add.
	 * @param value Value of the header key to add.
	 */
	public HeaderRequestInterceptor(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpRequestWrapper wrapper = new HttpRequestWrapper(request);

		if(name != null && value != null) {
			wrapper.getHeaders().set(name, value);
		}

		return execution.execute(wrapper, body);
	}
}
