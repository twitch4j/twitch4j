package me.philippheuer.twitch4j.helper;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

/**
 * @author http://stackoverflow.com/users/912950/ammar
 * @link http://stackoverflow.com/questions/19238715/how-to-set-an-accept-header-on-spring-resttemplate-request
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
