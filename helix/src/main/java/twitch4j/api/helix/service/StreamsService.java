package twitch4j.api.helix.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitch4j.api.helix.model.Game;
import twitch4j.api.helix.model.Pagination;
import twitch4j.api.helix.model.Stream;
import twitch4j.api.helix.model.StreamData;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.request.TwitchRequest;
import twitch4j.stream.rest.route.Route;

import javax.naming.SizeLimitExceededException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides Streams Service from specific data.<br>
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
@Slf4j
public class StreamsService extends AbstractService<StreamData> {
	private AtomicInteger size = new AtomicInteger(20);
	private Set<String> communityIds = new HashSet<>();
	private Set<Long> gameIds = new LinkedHashSet<>();
	private Set<Locale> languages = new LinkedHashSet<>();
	private Set<Long> userIds = new LinkedHashSet<>();
	private Set<String> usernames = new LinkedHashSet<>();

	public StreamsService(Router router) {
		super(Route.get("/streams", StreamData.class), router);
	}

	/**
	 * Limit data for pagination.
	 * @param size <b>Max:</b> 100,<br> <b>Default:</b> 20
	 * @return
	 */
	public StreamsService size(int size) {
		if (size > 100) {
			this.size.set(100);
		} else if (size < 1) {
			this.size.set(20);
		} else {
			this.size.set(size);
		}

		return this;
	}

	/**
	 * Community ID
	 * @param communityId community id
	 * @return
	 */
	public StreamsService communityId(String communityId) {
		if (communityIds.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of community_id's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.communityIds.add(communityId);
		}
		return this;
	}

	/**
	 * Game ID
	 * @param gameId
	 * @return
	 */
	public StreamsService gameId(Long gameId) {
		if (gameIds.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of game_id's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.gameIds.add(gameId);
		}
		return this;
	}

	/**
	 *
	 * @param game
	 * @return
	 */
	public StreamsService game(Game game) {
		return gameId(game.getId());
	}

	/**
	 *
	 * @param lang
	 * @return
	 */
	public StreamsService language(Locale lang) {
		if (gameIds.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of language's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.languages.add(lang);
		}
		return this;
	}

	/**
	 *
	 * @param userId
	 * @return
	 */
	public StreamsService userId(Long userId) {
		if (userIds.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of user_id's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.userIds.add(userId);
		}
		return this;
	}

	/**
	 *
	 * @param username
	 * @return
	 */
	public StreamsService username(String username) {
		if (usernames.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of login's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.usernames.add(username);
		}
		return this;
	}

	/**
	 *
	 * @return
	 */
	public Mono<Pagination<Stream, StreamData>> exchange() {
		TwitchRequest<StreamData> request = route.newRequest();

		if (!communityIds.isEmpty()) {
			communityIds.forEach(id -> request.query("community_id", id));
		}
		if (!gameIds.isEmpty()) {
			gameIds.forEach(id -> request.query("game_id", id.toString()));
		}
		if (!languages.isEmpty()) {
			languages.forEach(lang -> request.query("language", lang.getDisplayLanguage().toLowerCase()));
		}
		if (!userIds.isEmpty()) {
			userIds.forEach(id -> request.query("user_id", id.toString()));
		}
		if (!usernames.isEmpty()) {
			usernames.forEach(username -> request.query("user_login", username));
		}

		return request.exchange(router)
				.flatMap(d -> Mono.just(new Pagination<>(router, d, request)));
	}
}
