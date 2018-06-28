package twitch4j.api.helix.model;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.request.TwitchRequest;

@RequiredArgsConstructor
public class Pagination<T, P extends PaginationList<T>> implements Publisher<T> {
	private final Router router;
	private final P list;
	private final TwitchRequest<P> request;

	public Mono<Pagination<T, P>> next() {
		final TwitchRequest<P> oldRequest = this.request;
		return this.request.query("after", list.getPagination().getCursor())
				.exchange(router)
				.flatMap(p -> Mono.just(new Pagination<T, P>(router, p, oldRequest)));
	}

	public Mono<Pagination<T, P>> previous() {
		final TwitchRequest<P> oldRequest = this.request;
		return this.request.query("before", list.getPagination().getCursor())
				.exchange(router)
				.flatMap(p -> Mono.just(new Pagination<T, P>(router, p, oldRequest)));
	}

	public Flux<T> get() {
		return Flux.fromIterable(list.getData());
	}

	@Override
	public String toString() {
		return get().toString();
	}

	@Override
	public void subscribe(Subscriber<? super T> s) {
		get().subscribe(s);
	}
}
