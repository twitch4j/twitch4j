package twitch4j.api.helix.service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.route.Route;

@RequiredArgsConstructor
public abstract class AbstractService<T> {
	protected final Route<T> route;
	protected final Router router;
}
