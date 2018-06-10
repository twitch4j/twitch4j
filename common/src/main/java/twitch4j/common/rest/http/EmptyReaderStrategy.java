package twitch4j.common.rest.http;

import javax.annotation.Nullable;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.client.HttpClientResponse;

/**
 * Read a response without a body.
 */
public class EmptyReaderStrategy implements ReaderStrategy<Void> {

	@Override
	public boolean canRead(@Nullable Class<?> type, @Nullable String contentType) {
		return type != null && type == Void.class;
	}

	@Override
	public Mono<Void> read(HttpClientResponse response, Class<Void> responseType) {
		return Mono.empty();
	}
}
