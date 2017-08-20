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
			ex.printStackTrace();
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
			ex.printStackTrace();
			System.exit(0);
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
			ex.printStackTrace();
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
			ex.printStackTrace();
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
			ex.printStackTrace();
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
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the streams on the frontpage for a specific region (UnofficialEndpoint)
	 * <p>
	 * Valid Regions:
	 * AT: Austria
	 * BE: Belgium
	 * BG: Bulgaria
	 * CY: Cyprus
	 * CZ: Czech Republic
	 * DE: Germany
	 * DK: Denmark
	 * EE: Estonia
	 * FI: Finland
	 * FR: France
	 * GR: Greece
	 * GL: Greenland
	 * HU: Hungary
	 * IS: Iceland
	 * IT: Italy
	 * LT: Lithuania
	 * LU: Luxembourg
	 * NL: Netherlands
	 * NO: Norway
	 * PL: Poland
	 * PT: Portugal
	 * RO: Romania
	 * RU: Russia
	 * SK: Slovakia
	 * SI: Slovenia
	 * ES: Spain
	 * SE: Sweden
	 * CH: Switzerland
	 * TR: Turkey
	 * LV: Latvia
	 * MT: Malta
	 * RS: Serbia
	 * AL: Albania
	 * AD: Andorra
	 * AM: Armenia
	 * AZ: Azerbaijan
	 * BY: Belarus
	 * BA: Bosnia + Herzegovina
	 * HR: Croatia
	 * GE: Georgia
	 * IL: Israel
	 * LI: Liechtenstein
	 * MK: Macedonia
	 * MD: Moldova
	 * MC: Monaco
	 * ME: Montenegro
	 * QA: Qatar
	 * SM: San Marino
	 * UA: Ukraine
	 * UK: United Kingdom
	 * GB: Great Britain
	 * IE: Ireland
	 * US: USA
	 *
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
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Checks if a stream is live (includes replays)
	 * Requires Scope: none
	 *
	 * @param channel Get stream object of Channel Entity
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
	 * @param channel Get stream object of Channel Entity
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
	 * @return Whether a stream is on the frontpage.
	 */
	@Unofficial
	public Boolean isStreamOnFrontpage(Stream stream) {
		List<String> regions = Arrays.asList("", "");



		return false;
	}
}
