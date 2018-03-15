package io.twitch4j.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.ext.sync.Sync;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;

public class SynchronizedApi {
    public static <T> T buildResponseResult(HttpRequest<T> request) {
        return buildResult(request).body();
    }

    public static <T> T buildPojoResult(HttpRequest<Buffer> request, Class<T> responseType) {
        return buildPojoResult(request, new SimpleTypeReference<T>(responseType));
    }

    public static <T> T buildPojoResult(HttpRequest<Buffer> request, TypeReference<T> responseType) {
        HttpResponse<Buffer> response = buildResult(request);
        return Json.mapper.convertValue(response.body().toJsonObject(), responseType);
    }

    public static <T> HttpResponse<T> buildResult(HttpRequest<T> request) {
        return Sync.awaitResult(request::send);
    }
}
