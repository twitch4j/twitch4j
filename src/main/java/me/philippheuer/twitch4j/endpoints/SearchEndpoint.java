package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
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
public class SearchEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Search Endpoint
	 *
	 * @param twitchClient The TwitchClient.
	 */
	public SearchEndpoint(TwitchClient twitchClient) {
		super(twitchClient);
	}

	/**
	 * Endpoint: Search Channels
	 * Searches for channels based on a specified query parameter. A channel is returned if the query parameter is matched entirely or partially, in the channel description or game name.
	 * Requires Scope: none
	 *
	 * @param query search query
	 * @param limit Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @return A list of Channels matching the query.
	 */
	public List<Channel> getChannels(String query, Optional<Long> limit) {
		// Endpoint
		String requestUrl = String.format("%s/search/channels", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("query", query));

		// REST Request
		try {
			ChannelList responseObject = restTemplate.getForObject(requestUrl, ChannelList.class);

			return responseObject.getChannels();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Endpoint: Search Games
	 * Searches for games based on a specified query parameter. A game is returned if the query parameter is matched entirely or partially, in the game name.
	 * Requires Scope: none
	 *
	 * @param query search query
	 * @param live Whether only games that are live should be returned. This argument is optional.
	 * @return A list of games matching the query.
	 */
	public List<Game> getGames(String query, Optional<Boolean> live) {
		// Endpoint
		String requestUrl = String.format("%s/search/games", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("query", query));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("live", live.orElse(false).toString()));

		// REST Request
		try {
			GameList responseObject = restTemplate.getForObject(requestUrl, GameList.class);

			return responseObject.getGames();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Endpoint: Search Streams
	 * Searches for streams based on a specified query parameter. A stream is returned if the query parameter is matched entirely or partially, in the channel description or game name.
	 * Requires Scope: none
	 *
	 * @param query search query
	 * @param limit Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @return A list of Streams matching the query.
	 */
	public List<Stream> getStreams(String query, Optional<Long> limit) {
		// Endpoint
		String requestUrl = String.format("%s/search/streams", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("query", query));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			StreamList responseObject = restTemplate.getForObject(requestUrl, StreamList.class);

			return responseObject.getStreams();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}
}
