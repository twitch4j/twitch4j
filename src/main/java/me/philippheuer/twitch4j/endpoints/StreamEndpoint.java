package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.*;
import me.philippheuer.util.annotation.Unofficial;
import me.philippheuer.util.rest.QueryRequestInterceptor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * All api methods related to a stream.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@Setter
public class StreamEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Stream Endpoint
	 *
	 * @param twitchClient The Twitch Client.
	 */
	public StreamEndpoint(TwitchClient twitchClient) {
		super(twitchClient);
	}

	/**
	 * Get Stream by Channel
	 * <p>
	 * Gets stream information (the stream object) for a specified channel.
	 * Requires Scope: none
	 *
	 * @param channel Get stream object of Channel Entity
	 * @return Optional of type Stream is only Present if Stream is Online, returns Optional.empty for Offline streams
	 */
	public Optional<Stream> getByChannel(Channel channel) {
		// Endpoint
		String requestUrl = String.format("%s/streams/%s", Endpoints.API.getURL(), channel.getId());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			StreamSingle responseObject = restTemplate.getForObject(requestUrl, StreamSingle.class);

			// Stream Offline
			if (responseObject.getStream() == null) {
				// Stream Offline
				return Optional.empty();
			} else {
				// Stream Online
				return Optional.ofNullable(responseObject.getStream());
			}

		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Get All Streams (ordered by current viewers, desc)
	 * <p>
	 * Gets the list of all live streams.
	 * Requires Scope: none
	 *
	 * @param limit       Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @param offset      Object offset for pagination of results. Default: 0.
	 * @param language    Restricts the returned streams to the specified language. Permitted values are locale ID strings, e.g. en, fi, es-mx.
	 * @param game        Restricts the returned streams to the specified game.
	 * @param channelIds  Receives the streams from a comma-separated list of channel IDs.
	 * @param stream_type Restricts the returned streams to a certain stream type. Valid values: live, playlist, all. Playlists are offline streams of VODs (Video on Demand) that appear live. Default: live.
	 * @return Returns all streams that match with the provided filtering.
	 */
	public List<Stream> getAll(Optional<Long> limit, Optional<Long> offset, Optional<String> language, Optional<Game> game, Optional<String> channelIds, Optional<String> stream_type) {
		// Endpoint
		String requestUrl = String.format("%s/streams", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", offset.orElse(0l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("language", language.orElse(null)));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("game", game.map(Game::getName).orElse(null)));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("channel", channelIds.isPresent() ? channelIds.get() : null));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("stream_type", stream_type.orElse("all")));

		// REST Request
		try {
			StreamList responseObject = restTemplate.getForObject(requestUrl, StreamList.class);

			return responseObject.getStreams();
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Get Followed Streams
	 * <p>
	 * Gets the list of online streams a user follows based on the OAuthTwitch token provided.
	 * Requires Scope: user_read
	 *
	 * @param credential The user.
	 * @return All streams as user follows.
	 */
	public List<Stream> getFollowed(OAuthCredential credential) {
		// Endpoint
		String requestUrl = String.format("%s/streams/followed", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(credential);

		// REST Request
		try {
			StreamList responseObject = restTemplate.getForObject(requestUrl, StreamList.class);

			return responseObject.getStreams();
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return new ArrayList<Stream>();
	}

	/**
	 * Get Featured Streams
	 * <p>
	 * Gets a list of all featured live streams.
	 * Requires Scope: none
	 *
	 * @param limit  Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @param offset Object offset for pagination of results. Default: 0.
	 * @return The requested range/amount of featured streams.
	 */
	public List<StreamFeatured> getFeatured(Optional<Long> limit, Optional<Long> offset) {
		// Endpoint
		String requestUrl = String.format("%s/streams/featured", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", offset.orElse(0l).toString()));

		// REST Request
		try {
			StreamFeaturedList responseObject = restTemplate.getForObject(requestUrl, StreamFeaturedList.class);

			return responseObject.getFeatured();
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return new ArrayList<StreamFeatured>();
	}

	/**
	 * Get Streams Summary
	 * <p>
	 * Gets a summary of all live streams.
	 * Requires Scope: none
	 *
	 * @param game Restricts the summary stats to the specified game.
	 * @return A <code>StreamSummary</code> object, that contains the total number of live streams and viewers.
	 */
	public StreamSummary getSummary(Optional<Game> game) {
		// Endpoint
		String requestUrl = String.format("%s/streams/summary", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("game", game.map(Game::getName).orElse("")));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			StreamSummary responseObject = restTemplate.getForObject(requestUrl, StreamSummary.class);

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
	 * Get recommended streams for User (Unofficial)
	 * Gets a list of recommended streams for a user.
	 * Requires Scope: none
	 *
	 * @param credential OAuthCredential of the user, you want to request recommendations for.
	 * @return StreamList of random Streams.
	 */
	@Unofficial
	public List<Recommendation> getRecommendations(OAuthCredential credential) {
		// Endpoint
		String requestUrl = String.format("%s/streams/recommended", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("oauth_token", credential.getToken()));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			RecommendationList responseObject = restTemplate.getForObject(requestUrl, RecommendationList.class);

			return responseObject.getRecommendedStreams();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Get the streams on the frontpage for a specific region (UnofficialEndpoint)
	 * <table summary="Valid Regions:">
	 *     <tr><th>Code</th><th>Region</th></tr>
	 *     <tr><td>AT</td><td>Austria</td></tr>
	 *     <tr><td>BE</td><td>Belgium</td></tr>
	 *     <tr><td>BG</td><td>Bulgaria</td></tr>
	 *     <tr><td>CY</td><td>Cyprus</td></tr>
	 *     <tr><td>CZ</td><td>Czech Republic</td></tr>
	 *     <tr><td>DE</td><td>Germany</td></tr>
	 *     <tr><td>DK</td><td>Denmark</td></tr>
	 *     <tr><td>EE</td><td>Estonia</td></tr>
	 *     <tr><td>FI</td><td>Finland</td></tr>
	 *     <tr><td>FR</td><td>France</td></tr>
	 *     <tr><td>GR</td><td>Greece</td></tr>
	 *     <tr><td>GL</td><td>Greenland</td></tr>
	 *     <tr><td>HU</td><td>Hungary</td></tr>
	 *     <tr><td>IS</td><td>Iceland</td></tr>
	 *     <tr><td>IT</td><td>Italy</td></tr>
	 *     <tr><td>LT</td><td>Lithuania</td></tr>
	 *     <tr><td>LU</td><td>Luxembourg</td></tr>
	 *     <tr><td>NL</td><td>Netherlands</td></tr>
	 *     <tr><td>NO</td><td>Norway</td></tr>
	 *     <tr><td>PL</td><td>Poland</td></tr>
	 *     <tr><td>PT</td><td>Portugal</td></tr>
	 *     <tr><td>RO</td><td>Romania</td></tr>
	 *     <tr><td>RU</td><td>Russia</td></tr>
	 *     <tr><td>SK</td><td>Slovakia</td></tr>
	 *     <tr><td>SI</td><td>Slovenia</td></tr>
	 *     <tr><td>ES</td><td>Spain</td></tr>
	 *     <tr><td>SE</td><td>Sweden</td></tr>
	 *     <tr><td>CH</td><td>Switzerland</td></tr>
	 *     <tr><td>TR</td><td>Turkey</td></tr>
	 *     <tr><td>LV</td><td>Latvia</td></tr>
	 *     <tr><td>MT</td><td>Malta</td></tr>
	 *     <tr><td>RS</td><td>Serbia</td></tr>
	 *     <tr><td>AL</td><td>Albania</td></tr>
	 *     <tr><td>AD</td><td>Andorra</td></tr>
	 *     <tr><td>AM</td><td>Armenia</td></tr>
	 *     <tr><td>AZ</td><td>Azerbaijan</td></tr>
	 *     <tr><td>BY</td><td>Belarus</td></tr>
	 *     <tr><td>BA</td><td>Bosnia and Herzegovina</td></tr>
	 *     <tr><td>HR</td><td>Croatia</td></tr>
	 *     <tr><td>GE</td><td>Georgia</td></tr>
	 *     <tr><td>IL</td><td>Israel</td></tr>
	 *     <tr><td>LI</td><td>Liechtenstein</td></tr>
	 *     <tr><td>MK</td><td>Macedonia</td></tr>
	 *     <tr><td>MD</td><td>Moldova</td></tr>
	 *     <tr><td>MC</td><td>Monaco</td></tr>
	 *     <tr><td>ME</td><td>Montenegro</td></tr>
	 *     <tr><td>QA</td><td>Qatar</td></tr>
	 *     <tr><td>SM</td><td>San Marino</td></tr>
	 *     <tr><td>UA</td><td>Ukraine</td></tr>
	 *     <tr><td>UK</td><td>United Kingdom</td></tr>
	 *     <tr><td>GB</td><td>Great Britain</td></tr>
	 *     <tr><td>IE</td><td>Ireland</td></tr>
	 *     <tr><td>US</td><td>USA</td></tr>
	 * </table>
	 *
	 * @param geo using {@link Optional} Valid Code Regions below
	 * @return The 6 streams of the frontpage for the specified region.
	 */
	@Unofficial
	public List<StreamFeatured> getStreamsOnFrontpage(Optional<String> geo) {
		// Endpoint
		String requestUrl = String.format("https://api.twitch.tv/kraken/streams/featured");
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", "6"));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("geo", geo.orElse("US")));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("lang", "en"));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			StreamFeaturedList responseObject = restTemplate.getForObject(requestUrl, StreamFeaturedList.class);

			return responseObject.getFeatured();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Checks if a stream is live (includes replays)
	 * Requires Scope: none
	 *
	 * @param channel Get stream object of Channel Entity
	 * @return Channel is live
	 */
	public boolean isLive(Channel channel) {
		Optional<Stream> stream = this.getByChannel(channel);

		if(stream.isPresent()) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if a stream is currently live and running a replay
	 * Requires Scope: none
	 *
	 * @param channel Get stream object of Channel Entity.
	 * @return Channel is live and playing replay.
	 */
	public boolean isReplaying(Channel channel) {
		Optional<Stream> stream = this.getByChannel(channel);

		if(stream.isPresent()) {
			if(stream.get().isPlaylist()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Whether a stream is on the frontpage.
	 * <p>
	 * This method check's if the stream is on the frontpage for a certain region.
	 *
	 * @param stream Stream object
	 * @return Whether a stream is on the frontpage.
	 */
	@Unofficial
	public Boolean isStreamOnFrontpage(Stream stream) {
		List<String> regions = Arrays.asList("", "");



		return false;
	}
}
