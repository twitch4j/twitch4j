package twitch4j.api.helix.service;

import reactor.core.publisher.Flux;
import twitch4j.api.helix.json.*;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.request.TwitchRequest;
import twitch4j.stream.rest.route.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GamesService extends AbstractService<GamesData> {
	private final Route<TopGames> topStreams = Route.get("/games/top", TopGames.class);

	public GamesService(Router router) {
		super(Route.get("/games", GamesData.class), router);
	}

	public Flux<Game> getByIds(Long... ids) {
		return getByIds(Arrays.asList(ids));
	}

	public Flux<Game> getByNames(String... names) {
		return getByNames(Arrays.asList(names));
	}

	public Flux<Game> getByIds(Collection<Long> ids) {
		if (ids.size() > 100)
			ids = new ArrayList<>(ids).subList(0, 99);
		TwitchRequest<GamesData> request = route.newRequest();
		ids.forEach(id -> request.query("id", id.toString()));

		return request.exchange(router)
				.flatMapMany(data -> Flux.fromIterable(data.getData()));
	}

	public Flux<Game> getByNames(Collection<String> names) {
		if (names.size() > 100)
			names = new ArrayList<>(names).subList(0, 99);

		TwitchRequest<GamesData> request = route.newRequest();
		names.forEach(name -> request.query("name", name));

		return request.exchange(router)
				.flatMapMany(data -> Flux.fromIterable(data.getData()));
	}

	public Flux<Game> getTopGames() {
		return topStreams.newRequest()
				.exchange(router)
				.flatMapMany(top -> Flux.fromIterable(top.getData()));
	}
}
