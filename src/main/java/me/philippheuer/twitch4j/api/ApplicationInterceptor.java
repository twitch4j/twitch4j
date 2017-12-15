package me.philippheuer.twitch4j.api;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.oauth2.OAuth2Version;
import org.springframework.social.oauth2.TokenStrategy;
import org.springframework.social.support.HttpRequestDecorator;

import java.io.IOException;

@AllArgsConstructor
class ApplicationInterceptor implements ClientHttpRequestInterceptor {
    private final String clientId;
    private final String accessToken;
    private final OAuth2Version version;

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequest protectedResourceRequest = new HttpRequestDecorator(request);
        protectedResourceRequest.getHeaders().set("Client-ID", clientId);
        if (accessToken != null) {
            protectedResourceRequest.getHeaders().set("Authorization", version.getAuthorizationHeaderValue(accessToken));
        }
        return execution.execute(protectedResourceRequest, body);
    }
}
