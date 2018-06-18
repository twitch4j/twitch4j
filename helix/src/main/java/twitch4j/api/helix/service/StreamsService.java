package twitch4j.api.helix.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import twitch4j.api.helix.json.Clip;
import twitch4j.api.helix.json.ClipsData;
import twitch4j.api.helix.json.Stream;
import twitch4j.api.helix.json.StreamData;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.request.TwitchRequest;
import twitch4j.stream.rest.route.Route;

import javax.naming.SizeLimitExceededException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
	public StreamsService communityId(String communityId) {
		if (communityIds.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of community_id's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.communityIds.add(communityId);
		}
		return this;
	}
	public StreamsService gameId(Long gameId) {
		if (gameIds.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of game_id's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.gameIds.add(gameId);
		}
		return this;
	}
	public StreamsService language(Locale lang) {
		if (gameIds.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of language's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.languages.add(lang);
		}
		return this;
	}
	public StreamsService userId(Long userId) {
		if (userIds.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of user_id's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.userIds.add(userId);
		}
		return this;
	}
	public StreamsService username(String username) {
		if (usernames.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of login's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.usernames.add(username);
		}
		return this;
	}

	public Flux<Stream> exchange() {
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

		return request.exchange(router).flatMapMany(data -> Flux.fromIterable(data.getData()));
	}
}
