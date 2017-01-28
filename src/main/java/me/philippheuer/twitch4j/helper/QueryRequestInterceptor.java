package me.philippheuer.twitch4j.helper;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.support.HttpRequestDecorator;

import java.io.IOException;

/**
 * Query Request Interceptor
 */
public class QueryRequestInterceptor implements ClientHttpRequestInterceptor {

	private final String name;

    private final String value;

    public QueryRequestInterceptor(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequest httpRequest = new HttpRequestDecorator(request);
		httpRequest.getHeaders().set(name, value);
        return execution.execute(httpRequest, body);
    }
}
