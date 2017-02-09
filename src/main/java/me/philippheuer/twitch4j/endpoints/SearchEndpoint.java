package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.helper.QueryRequestInterceptor;
import me.philippheuer.twitch4j.model.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Getter
@Setter
public class SearchEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 */
	public SearchEndpoint(TwitchClient client) {
		super(client);
	}

	/**
	 * Endpoint: Search Channels
	 * Searches for channels based on a specified query parameter. A channel is returned if the query parameter is matched entirely or partially, in the channel description or game name.
	 * Requires Scope: none
	 * @param query search query
	 */
	public List<Channel> getChannels(String query) {
		// Endpoint
		String requestUrl = String.format("%s/search/channels", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("query", query));

		// REST Request
		try {
			ChannelList responseObject = restTemplate.getForObject(requestUrl, ChannelList.class);

			return responseObject.getChannels();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Endpoint: Search Games
	 * Searches for games based on a specified query parameter. A game is returned if the query parameter is matched entirely or partially, in the game name.
	 * Requires Scope: none
	 * @param query search query
	 */
	public List<Game> getGames(String query) {
		// Endpoint
		String requestUrl = String.format("%s/search/games", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("query", query));

		// REST Request
		try {
			GameList responseObject = restTemplate.getForObject(requestUrl, GameList.class);

			return responseObject.getGames();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Endpoint: Search Streams
	 * Searches for streams based on a specified query parameter. A stream is returned if the query parameter is matched entirely or partially, in the channel description or game name.
	 * Requires Scope: none
	 * @param query search query
	 */
	public List<Stream> getStreams(String query) {
		// Endpoint
		String requestUrl = String.format("%s/search/streams", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("query", query));

		// REST Request
		try {
			StreamList responseObject = restTemplate.getForObject(requestUrl, StreamList.class);

			return responseObject.getStreams();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
