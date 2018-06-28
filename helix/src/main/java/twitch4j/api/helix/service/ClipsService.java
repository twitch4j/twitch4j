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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides Clips Service for getting data from streamers and game specific clip.
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
public class ClipsService extends AbstractSizedService<ClipsData> {

	private volatile AtomicInteger size = new AtomicInteger(20);

	public ClipsService(Router router) {
		super(Route.get("/clips", ClipsData.class), router);
	}

	public Mono<Clip> getClip(String clipId) {
		return getAllClips(clipId).flatMap(p -> p.get().next());
	}

	public Mono<Pagination<Clip, ClipsData>> getAllClips(String... clipIds) {
		return getAllClips(Arrays.asList(clipIds));
	}

	public Mono<Pagination<Clip, ClipsData>> getAllClips(Collection<String> clipIds) {
		if (clipIds.size() > 100) {
			clipIds = new ArrayList<>(clipIds).subList(0, 99);
		}

		TwitchRequest<ClipsData> request = route.newRequest();
		clipIds.forEach(clip -> request.query("id", clip));

		return request.exchange(router)
				.flatMap(data -> Mono.just(new Pagination<>(router, data, request)));
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
}
