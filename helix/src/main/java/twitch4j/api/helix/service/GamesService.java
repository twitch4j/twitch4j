package twitch4j.api.helix.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitch4j.api.helix.model.*;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.request.TwitchRequest;
import twitch4j.stream.rest.route.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Provides Games Service. Searching {@link #getByNames(String...) by name}, {@link #getByIds(Long...) by game ID}
 * and getting {@link #getTopGames() top games} on the Twitch.
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
public class GamesService extends AbstractService<GamesData> {
	/**
	 * Routes
	 */
	private final Route<TopGames> topGamesRoute = Route.get("/games/top", TopGames.class);

	/**
	 * Games Service
	 *
	 * @param router Http Router
	 */
	public GamesService(Router router) {
		super(Route.get("/games", GamesData.class), router);
	}

	/**
	 * Get games by Ids
	 *
	 * @param ids A list of id's
	 * @return A list of games
	 */
	public Flux<Game> getByIds(Long... ids) {
		return getByIds(Arrays.asList(ids));
	}

	/**
	 * Get games by name
	 *
	 * @param names A list of name's
	 * @return A list of games
	 */
	public Flux<Game> getByNames(String... names) {
		return getByNames(Arrays.asList(names));
	}

	/**
	 * Get games by Ids
	 *
	 * @param ids A list of id's
	 * @return A list of games
	 */
	public Flux<Game> getByIds(Collection<Long> ids) {
		if (ids.size() > 100)
			ids = new ArrayList<>(ids).subList(0, 99);
		TwitchRequest<GamesData> request = route.newRequest();
		ids.forEach(id -> request.query("id", id.toString()));

		return request.exchange(router)
				.flatMapMany(data -> Flux.fromIterable(data.getData()));
	}

	/**
	 * Get games by name
	 *
	 * @param names A list of name's
	 * @return A list of games
	 */
	public Flux<Game> getByNames(Collection<String> names) {
		if (names.size() > 100)
			names = new ArrayList<>(names).subList(0, 99);

		TwitchRequest<GamesData> request = route.newRequest();
		names.forEach(name -> request.query("name", name));

		return request.exchange(router)
				.flatMapMany(data -> Flux.fromIterable(data.getData()));
	}

	/**
	 * Get top games
	 *
	 * @return The the list of games ordered by current viewers
	 */
	public Mono<Pagination<Game, TopGames>> getTopGames() {
		return topGamesRoute.newRequest()
				.exchange(router)
				.flatMap(t -> Mono.just(new Pagination<>(router, t, topGamesRoute.newRequest())));
	}
}
