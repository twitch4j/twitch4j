package twitch4j.endpoints;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;
import twitch4j.TwitchClient;
import twitch4j.auth.model.OAuthCredential;
import twitch4j.enums.BroadcastType;
import twitch4j.enums.Scope;
import twitch4j.enums.VideoPeriod;
import twitch4j.enums.VideoSort;
import twitch4j.model.Game;
import twitch4j.model.Video;
import twitch4j.model.VideoList;
import twitch4j.model.VideoTopList;
import twitch4j.util.rest.HeaderRequestInterceptor;
import twitch4j.util.rest.QueryRequestInterceptor;

@Slf4j
public class VideoEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 *
	 * @param client The Twitch Client.
	 */
	public VideoEndpoint(TwitchClient client) {
		super(client, client.getRestClient().getRestTemplate());
	}

	/**
	 * Endpoint: Get Video
	 * Gets a specified video object.
	 * Requires Scope: none
	 *
	 * @param videoId VideoID (int) to retrieve (the *v* prefix is deprecated in the v5 api)
	 * @return Returns a single video object.
	 */
	public Video getVideo(String videoId) {
		// Endpoint
		String requestUrl = String.format("/videos/%s", videoId);

		// REST Request
		try {
			return restTemplate.getForObject(requestUrl, Video.class);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Endpoint: Get Top Videos
	 * Gets the top videos based on viewcount, optionally filtered by game or time period.
	 * Requires Scope: none
	 *
	 * @param game          Constrains videos by game. A game name can be retrieved using the Search Games endpoint.
	 * @param period        Specifies the window of time to search. Valid values: week, month, all. Default: week
	 * @param broadcastType Constrains the type of videos returned. Valid values: (any combination of) archive, highlight, upload, Default: highlight. (comma-separated list)
	 * @return Returns all top videos matching the query parameters.
	 */
	public List<Video> getTopVideos(@Nullable Integer limit, @Nullable Integer offset, @Nullable Game game, @Nullable VideoPeriod period, @Nullable BroadcastType broadcastType, @Nullable List<Locale> language, @Nullable VideoSort sort) {
		// Endpoint
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
		}
		if (offset != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", Integer.toString((offset < 0) ? 0 : offset)));
		}
		if (game != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("game", game.getName()));
		}
		if (period != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("period", period.name().toLowerCase()));
		}
		if (broadcastType != null && !broadcastType.equals(BroadcastType.ALL)) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("broadcast_type", broadcastType.name().toLowerCase()));
		}
		if (language != null && language.size() > 0) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("language", language.stream().map(Locale::getLanguage).collect(Collectors.joining(","))));
		}
		if (sort != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("sort", sort.name().toLowerCase()));
		}

		// REST Request
		try {
			return restTemplate.getForObject("/videos/top", VideoTopList.class).getVods();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
	}

	/**
	 * Endpoint: Get Followed Videos
	 * Gets the videos from channels the user is following based on the OAuth token provided.
	 * Requires Scope: user_read
	 *
	 * @param credential    The user.
	 * @param broadcastType Constrains the type of videos returned. Valid values: (any combination of) archive, highlight, upload, Default: highlight. (comma-separated list)
	 * @return Gets the videos from channels the user is following based on the OAuth token provided.
	 */
	public List<Video> getFollowedVideos(OAuthCredential credential, @Nullable Integer limit, @Nullable Integer offset, @Nullable BroadcastType broadcastType, @Nullable List<Locale> language, @Nullable VideoSort sort) {
		try {
			checkScopePermission(credential.getOAuthScopes(), Scope.USER_READ);

			// Endpoint
			RestTemplate restTemplate = this.restTemplate;

			// Query Parameters
			if (limit != null) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
			}
			if (offset != null) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", Integer.toString((offset < 0) ? 0 : offset)));
			}
			if (broadcastType != null && !broadcastType.equals(BroadcastType.ALL)) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("broadcast_type", broadcastType.name().toLowerCase()));
			}
			if (language != null && language.size() > 0) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("language", language.stream().map(Locale::getLanguage).collect(Collectors.joining(","))));
			}
			if (sort != null) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("sort", sort.name().toLowerCase()));
			}

			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", "OAuth " + credential.getToken()));

			// REST Request
			return restTemplate.getForObject("/videos/followed", VideoList.class).getVideos();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
	}
}
