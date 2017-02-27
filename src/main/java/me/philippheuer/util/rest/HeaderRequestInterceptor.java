package me.philippheuer.util.rest;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

/**
 * TwitchClient is the core class for all api operations.
 * <p>
 * All coordinates which appear as arguments to the methods of this
 * Graphics object are considered relative to the translation origin
 * of this Graphics object prior to the invocation of the method.
 * All rendering operations modify only pixels which lie within the
 * area bounded by both the current clip of the graphics context
 * and the extents of the Component used to create the Graphics object.
 *
 * @author Philipp Heuer
 * @version %I%, %G%
 * @since 1.0
 */

/**
 * Spring Rest: Header Request Interceptor
 * <p>
 * The header request interceptors can be applied to a {@link org.springframework.web.client.RestTemplate} and
 * will add the header's at runtime.
 *
 * @author Philipp Heuer
 * @author ammar (http://stackoverflow.com/users/912950/ammar) on http://stackoverflow.com/questions/19238715/how-to-set-an-accept-header-on-spring-resttemplate-request}
 * @version %I%, %G%
 * @since 1.0
 */
public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {
	private final String name;

	private final String value;

	public HeaderRequestInterceptor(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpRequestWrapper wrapper = new HttpRequestWrapper(request);
		wrapper.getHeaders().set(name, value);
		return execution.execute(wrapper, body);
	}
}
