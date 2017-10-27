package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.*;
import me.philippheuer.util.rest.QueryRequestInterceptor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class VideoEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 *
	 * @param twitchClient The Twitch Client.
	 */
	public VideoEndpoint(TwitchClient twitchClient) {
		super(twitchClient);
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
		String requestUrl = String.format("%s/videos/%s", Endpoints.API.getURL(), videoId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			Video responseObject = restTemplate.getForObject(requestUrl, Video.class);

			return responseObject;
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Endpoint: Get Top Videos
	 * Gets the top videos based on viewcount, optionally filtered by game or time period.
	 * Requires Scope: none
	 * @param game Constrains videos by game. A game name can be retrieved using the Search Games endpoint.
	 * @param period Specifies the window of time to search. Valid values: week, month, all. Default: week
	 * @param broadcast_type Constrains the type of videos returned. Valid values: (any combination of) archive, highlight, upload, Default: highlight. (comma-separated list)
	 * @return Returns all top videos matching the query parameters.
	 */
	public List<Video> getTopVideos(Optional<Game> game, Optional<String> period, Optional<String> broadcast_type) {
		// Endpoint
		String requestUrl = String.format("%s/videos/top", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("game", game.map(Game::getName).orElse("")));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("period", period.orElse("week")));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("broadcast_type", broadcast_type.orElse("highlight")));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			VideoTopList responseObject = restTemplate.getForObject(requestUrl, VideoTopList.class);

			return responseObject.getVods();
		} catch (RestException restException) {
			RestError restError = restException.getRestError();
			Logger.error(this, "RestException: " + restError.toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		throw new RuntimeException("Unhandled Exception!");
	}

	/**
	 * Endpoint: Get Followed Videos
	 * Gets the videos from channels the user is following based on the OAuth token provided.
	 * Requires Scope: user_read
	 *
	 * @param oAuthCredential The user.
	 * @param broadcast_type Constrains the type of videos returned. Valid values: (any combination of) archive, highlight, upload, Default: highlight. (comma-separated list)
	 * @return Gets the videos from channels the user is following based on the OAuth token provided.
	 */
	public List<Video> getFollowedVideos(OAuthCredential oAuthCredential, Optional<String> broadcast_type) {
		// Endpoint
		String requestUrl = String.format("%s/videos/followed", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(oAuthCredential);

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("broadcast_type", broadcast_type.orElse("highlight")));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			VideoList responseObject = restTemplate.getForObject(requestUrl, VideoList.class);

			return responseObject.getVideos();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}
}
