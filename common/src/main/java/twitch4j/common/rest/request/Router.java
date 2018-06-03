package twitch4j.common.rest.request;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;
import reactor.util.function.Tuples;
import twitch4j.common.rest.http.client.SimpleHttpClient;

/**
 * Facilitates the routing of {@link TwitchRequest TwitchRequests} to the proper
 * {@link RequestStream RequestStream} according to the bucket in which the request falls.
 */
public class Router {

	@Getter
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
	 * @param <T>     The request's response type.
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
