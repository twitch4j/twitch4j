package twitch4j.api.helix.service;

import reactor.core.publisher.Flux;
import twitch4j.api.helix.model.Follow;
import twitch4j.api.helix.model.Follows;
import twitch4j.api.helix.model.User;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.route.Route;

/**
 * Provides Follows Service. Listing all follows.
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
public class FollowsService extends AbstractService<Follows> {
	public FollowsService(Router router) {
		super(Route.get("/users/follows", Follows.class), router);
	}

	/**
	 * Following from user.
	 * @param user following user
	 * @return list all follows
	 */
	public Flux<Follow> from(User user) {
		return route.newRequest()
				.query("from_id", user.getId().toString())
				.exchange(router)
				.flatMapMany(l -> Flux.fromIterable(l.getData()));
	}

	/**
	 * Following to user.
	 * @param user followed user
	 * @return list all follows
	 */
	public Flux<Follow> to(User user) {
		return route.newRequest()
				.query("to_id", user.getId().toString())
				.exchange(router)
				.flatMapMany(l -> Flux.fromIterable(l.getData()));
	}
}
