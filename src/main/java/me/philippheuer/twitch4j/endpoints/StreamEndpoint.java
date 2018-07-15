package me.philippheuer.twitch4j.endpoints;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.Scope;
import me.philippheuer.twitch4j.enums.StreamType;
import me.philippheuer.twitch4j.exceptions.ChannelCredentialMissingException;
import me.philippheuer.twitch4j.exceptions.ScopeMissingException;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.Game;
import me.philippheuer.twitch4j.model.Recommendation;
import me.philippheuer.twitch4j.model.RecommendationList;
import me.philippheuer.twitch4j.model.Stream;
import me.philippheuer.twitch4j.model.StreamFeatured;
import me.philippheuer.twitch4j.model.StreamFeaturedList;
import me.philippheuer.twitch4j.model.StreamList;
import me.philippheuer.twitch4j.model.StreamSingle;
import me.philippheuer.twitch4j.model.StreamSummary;
import me.philippheuer.twitch4j.model.User;
import me.philippheuer.util.annotation.Unofficial;
import me.philippheuer.util.rest.HeaderRequestInterceptor;
import me.philippheuer.util.rest.QueryRequestInterceptor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;

/**
 * All api methods related to a stream.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Slf4j
public class StreamEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Stream Endpoint
	 *
	 * @param client The Twitch Client.
	 */
	public StreamEndpoint(TwitchClient client) {
		super(client, client.getRestClient().getRestTemplate());
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
	public Stream getByChannel(Channel channel) {
		// Endpoint
		String requestUrl = String.format("/streams/%s", channel.getId());

		// REST Request
		try {
			return restTemplate.getForObject(requestUrl, StreamSingle.class).getStream();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return null;
		}
	}

	/**
	 * Get Stream by Channel
	 * <p>
	 * Gets stream information (the stream object) for a specified channel.
	 * Requires Scope: none
	 *
	 * @param user Get stream object of Channel Entity
	 * @return Optional of type Stream is only Present if Stream is Online, returns Optional.empty for Offline streams
	 */
	public Stream getByUser(User user) {
		// Endpoint
		String requestUrl = String.format("/streams/%s", user.getId());

		// REST Request
		try {
			return restTemplate.getForObject(requestUrl, StreamSingle.class).getStream();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return null;
		}
	}

	/**
	 * Get All Streams (ordered by current viewers, desc)
	 * <p>
	 * Gets the list of all live streams.
	 * Requires Scope: none
	 *
	 * @param limit      Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @param offset     Object offset for pagination of results. Default: 0.
	 * @param language   Restricts the returned streams to the specified language. Permitted values are locale ID strings, e.g. en, fi, es-mx.
	 * @param game       Restricts the returned streams to the specified game.
	 * @param channels   Receives the streams from a list of channels.
	 * @param streamType Restricts the returned streams to a certain stream type. Valid values: live, playlist, all. Playlists are offline streams of VODs (Video on Demand) that appear live. Default: live.
	 * @return Returns all streams that match with the provided filtering.
	 */
	public List<Stream> getLiveStreams(@Nullable List<Channel> channels, @Nullable Game game, @Nullable List<Locale> language, StreamType streamType, Integer limit, Integer offset) {
		// Endpoint
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
		}
		if (offset != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", Integer.toString((offset < 0) ? 0 : offset)));
		}
		if (language != null && language.size() > 0) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("language", language.stream().map(Locale::getLanguage).collect(Collectors.joining(","))));
		}
		if (game != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("game", game.getName()));
		}
		if (channels != null && channels.size() > 0) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("channel", channels.stream().map(channel -> channel.getId().toString()).collect(Collectors.joining(","))));
		}
		if (streamType != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("stream_type", streamType.name().toLowerCase()));
		}

		// REST Request
		try {
			return restTemplate.getForObject("/streams", StreamList.class).getStreams();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
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

		try {
			checkScopePermission(credential.getOAuthScopes(), Scope.USER_READ);

			// Endpoint
			RestTemplate restTemplate = this.restTemplate;

			// Parameters
			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.getToken())));

			// REST Request
			return restTemplate.getForObject("/streams/followed", StreamList.class).getStreams();
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.getUserId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
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
	public List<StreamFeatured> getFeatured(@Nullable Integer limit, @Nullable Integer offset) {
		// Endpoint
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
		}
		if (offset != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", Integer.toString((offset < 0) ? 0 : offset)));
		}

		// REST Request
		try {
			return restTemplate.getForObject("/streams/featured", StreamFeaturedList.class).getFeatured();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
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
	public StreamSummary getSummary(@Nullable Game game) {
		// Endpoint
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (game != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("game", game.getName()));
		}

		// REST Request
		try {
			return restTemplate.getForObject("/streams/summary", StreamSummary.class);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return new StreamSummary();
		}
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
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.getToken())));

		// REST Request
		try {
			return restTemplate.getForObject("/streams/recommended", RecommendationList.class).getRecommendedStreams();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
	}

	/**
	 * Get the streams on the frontpage for a specific region (UnofficialEndpoint)
	 * <table summary="Valid Regions:">
	 * <tr><th>Code</th><th>Region</th></tr>
	 * <tr><td>AT</td><td>Austria</td></tr>
	 * <tr><td>BE</td><td>Belgium</td></tr>
	 * <tr><td>BG</td><td>Bulgaria</td></tr>
	 * <tr><td>CY</td><td>Cyprus</td></tr>
	 * <tr><td>CZ</td><td>Czech Republic</td></tr>
	 * <tr><td>DE</td><td>Germany</td></tr>
	 * <tr><td>DK</td><td>Denmark</td></tr>
	 * <tr><td>EE</td><td>Estonia</td></tr>
	 * <tr><td>FI</td><td>Finland</td></tr>
	 * <tr><td>FR</td><td>France</td></tr>
	 * <tr><td>GR</td><td>Greece</td></tr>
	 * <tr><td>GL</td><td>Greenland</td></tr>
	 * <tr><td>HU</td><td>Hungary</td></tr>
	 * <tr><td>IS</td><td>Iceland</td></tr>
	 * <tr><td>IT</td><td>Italy</td></tr>
	 * <tr><td>LT</td><td>Lithuania</td></tr>
	 * <tr><td>LU</td><td>Luxembourg</td></tr>
	 * <tr><td>NL</td><td>Netherlands</td></tr>
	 * <tr><td>NO</td><td>Norway</td></tr>
	 * <tr><td>PL</td><td>Poland</td></tr>
	 * <tr><td>PT</td><td>Portugal</td></tr>
	 * <tr><td>RO</td><td>Romania</td></tr>
	 * <tr><td>RU</td><td>Russia</td></tr>
	 * <tr><td>SK</td><td>Slovakia</td></tr>
	 * <tr><td>SI</td><td>Slovenia</td></tr>
	 * <tr><td>ES</td><td>Spain</td></tr>
	 * <tr><td>SE</td><td>Sweden</td></tr>
	 * <tr><td>CH</td><td>Switzerland</td></tr>
	 * <tr><td>TR</td><td>Turkey</td></tr>
	 * <tr><td>LV</td><td>Latvia</td></tr>
	 * <tr><td>MT</td><td>Malta</td></tr>
	 * <tr><td>RS</td><td>Serbia</td></tr>
	 * <tr><td>AL</td><td>Albania</td></tr>
	 * <tr><td>AD</td><td>Andorra</td></tr>
	 * <tr><td>AM</td><td>Armenia</td></tr>
	 * <tr><td>AZ</td><td>Azerbaijan</td></tr>
	 * <tr><td>BY</td><td>Belarus</td></tr>
	 * <tr><td>BA</td><td>Bosnia and Herzegovina</td></tr>
	 * <tr><td>HR</td><td>Croatia</td></tr>
	 * <tr><td>GE</td><td>Georgia</td></tr>
	 * <tr><td>IL</td><td>Israel</td></tr>
	 * <tr><td>LI</td><td>Liechtenstein</td></tr>
	 * <tr><td>MK</td><td>Macedonia</td></tr>
	 * <tr><td>MD</td><td>Moldova</td></tr>
	 * <tr><td>MC</td><td>Monaco</td></tr>
	 * <tr><td>ME</td><td>Montenegro</td></tr>
	 * <tr><td>QA</td><td>Qatar</td></tr>
	 * <tr><td>SM</td><td>San Marino</td></tr>
	 * <tr><td>UA</td><td>Ukraine</td></tr>
	 * <tr><td>UK</td><td>United Kingdom</td></tr>
	 * <tr><td>GB</td><td>Great Britain</td></tr>
	 * <tr><td>IE</td><td>Ireland</td></tr>
	 * <tr><td>US</td><td>USA</td></tr>
	 * </table>
	 *
	 * @param locale using {@link Optional} Valid Code Regions below
	 * @return The 6 streams of the frontpage for the specified region.
	 */
	@Unofficial
	public List<StreamFeatured> getStreamsOnFrontpage(@Nullable Locale locale, @Nullable Integer limit) {
		// Endpoint
		String requestUrl = String.format("https://api.twitch.tv/kraken/streams/featured");
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
		}
		if (locale != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("lang", locale.getLanguage()));
			if (locale.getCountry() != null) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("geo", locale.getCountry()));
			}
		}

		// REST Request
		try {
			return restTemplate.getForObject(requestUrl, StreamFeaturedList.class).getFeatured();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
	}

	/**
	 * Checks if a stream is live (includes replays)
	 * Requires Scope: none
	 *
	 * @param channel Get stream object of Channel Entity
	 * @return Channel is live
	 */
	public boolean isLive(Channel channel) {
		return this.getByChannel(channel) != null;
	}

	/**
	 * Checks if a stream is currently live and running a replay
	 * Requires Scope: none
	 *
	 * @param channel Get stream object of Channel Entity.
	 * @return Channel is live and playing replay.
	 */
	public boolean isReplaying(Channel channel) {
		Stream stream = this.getByChannel(channel);
		return stream != null && stream.isPlaylist();
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
