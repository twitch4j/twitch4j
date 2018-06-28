package twitch4j.api.helix.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitch4j.api.helix.enums.Period;
import twitch4j.api.helix.enums.VideoSort;
import twitch4j.api.helix.model.*;
import twitch4j.common.enums.VideoType;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.request.TwitchRequest;
import twitch4j.stream.rest.route.Route;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Provides Video Service. <br>
 * <b>Required: </b> {@link twitch4j.common.auth.Scope#CLIPS_EDIT clips:edit}
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
public class VideoService extends AbstractSizedService<VideoList> {

	private final AtomicInteger size = new AtomicInteger(20);
	private final AtomicReference<Period> period = new AtomicReference<>();
	private final AtomicReference<VideoType> type = new AtomicReference<>();
	private final AtomicReference<VideoSort> sort = new AtomicReference<>();
	private final AtomicReference<Locale> language = new AtomicReference<>();


	public VideoService(Router router) {
		super(Route.get("/videos", VideoList.class), router);
	}

	public Mono<Video> getVideo(Long videoId) {
		return getVideos(videoId).next();
	}

	/**
	 * Getting videos by id
	 * @param videoIds
	 * @return
	 */
	public Flux<Video> getVideos(Long... videoIds) {
		return getVideos(Arrays.asList(videoIds));
	}

	public Flux<Video> getVideos(Collection<Long> videoIds) {
		if (videoIds.size() > 100) {
			videoIds = new ArrayList<>(videoIds).subList(0, 99);
		}

		TwitchRequest<VideoList> request = route.newRequest();
		videoIds.forEach(id -> request.query("id", id.toString()));

		return request.exchange(router).flatMapMany(data -> Flux.fromIterable(data.getData()));
	}

	public VideoService language(Locale language) {
		this.language.set(language);
		return this;
	}

	public VideoService period(Period period) {
		this.period.set(period);
		return this;
	}

	public VideoService sort(VideoSort sort) {
		this.sort.set(sort);
		return this;
	}

	public VideoService type(VideoType type) {
		this.type.set(type);
		return this;
	}

	private TwitchRequest<VideoList> request() {
		TwitchRequest<VideoList> request = route.newRequest();
		if (size.get() != 20) {
			request.query("first", size.get());
		}
		if (language.get() != null) {
			request.query("language", language.get().getDisplayLanguage());
		}
		if (period.get() != null) {
			request.query("period", period.get().name().toLowerCase());
		}
		if (sort.get() != null) {
			request.query("sort", sort.get().name().toLowerCase());
		}
		if (type.get() != null) {
			request.query("type", type.get().name().toLowerCase());
		}

		return request;
	}


	/**
	 * Getting videos from user specific
	 * @param user
	 * @return
	 */
	public Mono<Pagination<Video, VideoList>> getFromUser(User user) {
		TwitchRequest<VideoList> request = request()
				.query("user_id", user.getId().toString());

		return request.exchange(router)
				.flatMap(d -> Mono.just(new Pagination<>(router, d, request)));
	}

	/**
	 * Getting videos from game specific
	 * @param game
	 * @return
	 */
	public Mono<Pagination<Video, VideoList>> getFromGame(Game game) {
		TwitchRequest<VideoList> request = request()
				.query("game_id", game.getId().toString());

		return request.exchange(router)
				.flatMap(d -> Mono.just(new Pagination<>(router, d, request)));
	}
}
