package io.twitch4j.impl.api;

import io.twitch4j.IClient;
import io.twitch4j.ITwitchClient;
import io.twitch4j.api.IApi;
import io.twitch4j.impl.TwitchClientImpl;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import lombok.Getter;

import java.util.Map;

public abstract class ApiImpl implements IApi {
    @Getter
    private final IClient client;
    private final WebClient web;

    public ApiImpl(ITwitchClient client) {
        this.client = client;
        WebClientOptions opts = new WebClientOptions()
                .setUserAgent(client.getConfiguration().getUserAgent())
                .setUserAgentEnabled(true);
        this.web = WebClient.create(((TwitchClientImpl) client).getVertx(), opts);
    }

    @Override
    public void requestAsync(
            HttpMethod method,
            String url,
            Map<String, String> headers,
            Buffer body,
            Handler<AsyncResult<HttpResponse<Buffer>>> responseHandler) {
        HttpRequest<Buffer> request = this.web.request(method, url);
        if (headers.size() > 0) {
            headers.forEach(request::putHeader);
        }
        if (body != null && !(method.equals(HttpMethod.GET) || method.equals(HttpMethod.DELETE))) {
            request.sendBuffer(body, responseHandler);
        } else {
            request.send(responseHandler);
        }
    }
}
