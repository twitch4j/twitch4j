/*
 * MIT License
 *
 * Copyright (c) 2018 twitch4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.twitch4j.api;

import com.fasterxml.jackson.core.type.TypeReference;
import io.twitch4j.IClient;
import io.twitch4j.api.model.ErrorResponse;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.sync.Sync;
import io.vertx.ext.web.client.HttpResponse;

import java.lang.reflect.Type;
import java.util.Map;

public interface IApi {

    IClient getClient();

    void requestAsync(
			HttpMethod method,
			String url,
			Map<String, String> headers,
			Buffer body,
			Handler<AsyncResult<HttpResponse<Buffer>>> responseHandler);

    /**
     * Synchronous Request API.
     *
     * @param method  HTTP Type Method
     * @param url     url
     * @param headers Stringify {@link Map} of Headers
     * @param body    Body buffer
     * @return Response HTTP by Type response
     */
    default HttpResponse<Buffer> request(
			HttpMethod method,
			String url,
			Map<String, String> headers,
			Buffer body) {
        HttpResponse<Buffer> response = Sync.awaitResult(consumer -> requestAsync(method, url, headers, body, consumer));
        if (response.statusCode() >= 400) {
            throw new ApiException(response.bodyAsJson(ErrorResponse.class));
        }
        return response;
    }

    default HttpResponse<Buffer> get(String url, Map<String, String> headers) {
        return request(HttpMethod.GET, url, headers, null);
    }

    default HttpResponse<Buffer> post(String url, Map<String, String> headers, Buffer body) {
        return request(HttpMethod.POST, url, headers, body);
    }

    default HttpResponse<Buffer> put(String url, Map<String, String> headers, Buffer body) {
        return request(HttpMethod.PUT, url, headers, body);
    }

    default HttpResponse<Buffer> patch(String url, Map<String, String> headers, Buffer body) {
        return request(HttpMethod.PATCH, url, headers, body);
    }

    default HttpResponse<Buffer> delete(String url, Map<String, String> headers) {
        return request(HttpMethod.DELETE, url, headers, null);
    }

    default void getAsync(String url, Map<String, String> headers, Handler<AsyncResult<HttpResponse<Buffer>>> responseHandler) {
        requestAsync(HttpMethod.GET, url, headers, null, responseHandler);
    }

    default void postAsync(String url, Map<String, String> headers, Buffer body, Handler<AsyncResult<HttpResponse<Buffer>>> responseHandler) {
        requestAsync(HttpMethod.POST, url, headers, body, responseHandler);
    }

    default void putAsync(String url, Map<String, String> headers, Buffer body, Handler<AsyncResult<HttpResponse<Buffer>>> responseHandler) {
        requestAsync(HttpMethod.PUT, url, headers, body, responseHandler);
    }

    default void patchAsync(String url, Map<String, String> headers, Buffer body, Handler<AsyncResult<HttpResponse<Buffer>>> responseHandler) {
        requestAsync(HttpMethod.PATCH, url, headers, body, responseHandler);
    }

    default void deleteAsync(String url, Map<String, String> headers, Handler<AsyncResult<HttpResponse<Buffer>>> responseHandler) {
        requestAsync(HttpMethod.DELETE, url, headers, null, responseHandler);
    }

    <T> T buildPOJO(HttpResponse<Buffer> response, TypeReference<T> typeResponse) throws RuntimeException;

    default <T> T buildPOJO(HttpResponse<Buffer> response, Class<T> typeResponse) throws RuntimeException {
        return buildPOJO(response, new TypeReference<T>() {
            @Override
            public Type getType() {
                return typeResponse;
            }
        });
    }
}
