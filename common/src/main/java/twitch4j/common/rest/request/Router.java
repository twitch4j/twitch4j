/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J.  If not, see <http://www.gnu.org/licenses/>.
 */
package twitch4j.common.rest.request;

import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;
import reactor.util.function.Tuples;
import twitch4j.common.rest.http.client.SimpleHttpClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Facilitates the routing of {@link TwitchRequest TwitchRequests} to the proper
 * {@link RequestStream RequestStream} according to the bucket in which the request falls.
 */
public class Router {

    private final SimpleHttpClient httpClient;
    private final GlobalRateLimiter globalRateLimiter = new GlobalRateLimiter();
    private final Map<BucketKey, RequestStream<?>> streamMap = new ConcurrentHashMap<>();

    public Router(SimpleHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Queues a request for execution in the appropriate {@link RequestStream request stream}
     * according to the request's {@link BucketKey bucket}.
     *
     * @param request The request to queue.
     * @param <T> The request's response type.
     * @return A mono that receives signals based on the request's response.
     */
    public <T> Mono<T> exchange(TwitchRequest<T> request) {
        return Mono.defer(() -> {
            RequestStream<T> stream = getStream(request);
            MonoProcessor<T> callback = MonoProcessor.create();

            stream.push(Tuples.of(callback, request));
            return callback;
        });
    }

    @SuppressWarnings("unchecked")
    private <T> RequestStream<T> getStream(TwitchRequest<T> request) {
        return (RequestStream<T>)
                streamMap.computeIfAbsent(BucketKey.of(request.getRoute().getUriTemplate(), request.getCompleteUri()),
                        k -> {
                            RequestStream<T> stream = new RequestStream<>(httpClient, globalRateLimiter);
                            stream.start();
                            return stream;
                        });
    }
}
