package twitch4j.api.helix.service;

import reactor.core.publisher.Mono;
import twitch4j.api.helix.model.Follows;
import twitch4j.api.helix.model.User;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.route.Route;

public class FollowsService extends AbstractService<Follows> {
	public FollowsService(Router router) {
		super(Route.get("/users/follows", Follows.class), router);
	}

	public Mono<Follows> from(User user) {
		return route.newRequest()
				.query("from_id", user.getId().toString())
				.exchange(router);
	}

	public Mono<Follows> to(User user) {
		return route.newRequest()
				.query("to_id", user.getId().toString())
				.exchange(router);
	}
}
