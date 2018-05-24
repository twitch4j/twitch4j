package twitch4j.api.kraken.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;
import twitch4j.api.kraken.json.*;
import twitch4j.api.util.rest.QueryRequestInterceptor;

import javax.annotation.Nullable;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

@Slf4j
public class SearchEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Search Endpoint
	 *
	 * @param restTemplate {@link RestTemplate}
	 */
	public SearchEndpoint(RestTemplate restTemplate) {
		super(restTemplate);
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
	public List<Channel> getChannels(String query, @Nullable Integer limit) {
		// Endpoint
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
		}
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("query", URLEncoder.encode(query)));

		// REST Request
		try {
			return restTemplate.getForObject("/search/channels", ChannelList.class).getChannels();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
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
	public List<Game> getGames(String query, @Nullable Boolean live) {
		// Endpoint
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("query", query));
		if (live != null && live) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("live", live.toString()));
		}

		// REST Request
		try {
			return restTemplate.getForObject("/search/games", GameList.class).getGames();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
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
	public List<Stream> getStreams(String query, @Nullable Integer limit) {
		// Endpoint
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
		}
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("query", query));

		// REST Request
		try {
			return restTemplate.getForObject("/search/streams", StreamList.class).getStreams();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
	}
}
