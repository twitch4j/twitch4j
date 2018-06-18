package twitch4j.api.helix.service;

import reactor.core.publisher.Flux;
import twitch4j.api.helix.enums.Period;
import twitch4j.api.helix.enums.VideoSort;
import twitch4j.api.helix.json.Game;
import twitch4j.api.helix.json.User;
import twitch4j.api.helix.json.Video;
import twitch4j.api.helix.json.VideoList;
import twitch4j.common.enums.VideoType;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.request.TwitchRequest;
import twitch4j.stream.rest.route.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class VideoService extends AbstractService<VideoList> {

	private final AtomicInteger size = new AtomicInteger(20);
	private Period period;
	private VideoType type;
	private VideoSort sort;
	private Locale language;


	public VideoService(Router router) {
		super(Route.get("/videos", VideoList.class), router);
	}

	public Flux<Video> get(Long... videoIds) {
		return get(Arrays.asList(videoIds));
	}

	public Flux<Video> get(Collection<Long> videoIds) {
		if (videoIds.size() > 100) {
			videoIds = new ArrayList<>(videoIds).subList(0, 99);
		}

		TwitchRequest<VideoList> request = route.newRequest();
		videoIds.forEach(id -> request.query("id", id.toString()));

		return request.exchange(router).flatMapMany(data -> Flux.fromIterable(data.getData()));
	}

	public VideoService language(Locale language) {
		this.language = language;
		return this;
	}

	public VideoService period(Period period) {
		this.period = period;
		return this;
	}

	public VideoService sort(VideoSort sort) {
		this.sort = sort;
		return this;
	}

	public VideoService type(VideoType type) {
		this.type = type;
		return this;
	}

	public VideoService size(int size) {
		if (size > 100) {
			this.size.set(100);
		} else if (size < 1) {
			this.size.set(20);
		} else {
			this.size.set(size);
		}
		return this;
	}

	private TwitchRequest<VideoList> request() {
		TwitchRequest<VideoList> request = route.newRequest();
		if (size.get() != 20) {
			request.query("first", size.get());
		}
		if (language != null) {
			request.query("language", language.getDisplayLanguage());
		}
		if (period != null) {
			request.query("period", period.name().toLowerCase());
		}
		if (sort != null) {
			request.query("sort", sort.name().toLowerCase());
		}
		if (type != null) {
			request.query("type", type.name().toLowerCase());
		}

		return request;
	}

	public Flux<Video> getFromUser(User user) {
		return request()
				.query("user_id", user.getId().toString())
				.exchange(router)
				.flatMapMany(data -> Flux.fromIterable(data.getData()));
	}

	public Flux<Video> getFromGame(Game game) {
		return request()
				.query("game_id", game.getId().toString())
				.exchange(router)
				.flatMapMany(data -> Flux.fromIterable(data.getData()));
	}
}
