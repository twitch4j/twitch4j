package twitch4j.api.helix.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitch4j.api.helix.json.*;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.request.TwitchRequest;
import twitch4j.stream.rest.route.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ClipsService extends AbstractService<ClipsData> {

	private volatile AtomicInteger size = new AtomicInteger(20);

	public ClipsService(Router router) {
		super(Route.get("/clips", ClipsData.class), router);
	}

	public Mono<Clip> getClip(String clipId) {
		return getAllClips(clipId).next();
	}

	public Flux<Clip> getAllClips(String... clipIds) {
		return getAllClips(Arrays.asList(clipIds));
	}

	public Flux<Clip> getAllClips(Collection<String> clipIds) {
		if (clipIds.size() > 100) {
			clipIds = new ArrayList<>(clipIds).subList(0, 99);
		}

		TwitchRequest<ClipsData> request = route.newRequest();
		clipIds.forEach(clip -> request.query("id", clip));

		return request.exchange(router)
				.flatMapMany(data -> Flux.fromIterable(data.getData()));
	}

	public Flux<Clip> getByGame(Game game) {
		return get("game_id", game.getId().toString());
	}

	public Flux<Clip> getByBroadcaster(User user) {
		return get("broadcaster_id", user.getId().toString());
	}

	private Flux<Clip> get(String key, String value) {
		TwitchRequest<ClipsData> request = route.newRequest()
				.query("first", size)
				.query(key, value);

		return request.exchange(router).flatMapMany(clipsData -> Flux.fromIterable(clipsData.getData()));
	}

	public ClipsService size(int size) {
		if (size > 100) {
			this.size.set(100);
		} else if (size < 1) {
			this.size.set(20);
		} else {
			this.size.set(size);
		}
		return this;
	}
}
