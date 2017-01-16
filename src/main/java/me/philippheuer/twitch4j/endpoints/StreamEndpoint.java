package me.philippheuer.twitch4j.endpoints;

import java.util.List;
import java.util.Optional;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.model.*;

@Getter
@Setter
public class StreamEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Stream Endpoint
	 */
	public StreamEndpoint(TwitchClient client) {
		super(client);
	}

	/**
	 * Endpoint: Get Stream by Channel
	 *  Gets stream information (the stream object) for a specified channel.
	 * Requires Scope: none
	 */
	public Optional<Stream> getByChannel(Channel channel) {
		// REST Request
		try {
			String requestUrl = String.format("%s/streams/%s", getTwitchClient().getTwitchEndpoint(), channel.getId());
			StreamSingle responseObject = getTwitchClient().getRestClient().getRestTemplate().getForObject(requestUrl, StreamSingle.class);

			return Optional.ofNullable(responseObject.getStream());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	/**
	 * Endpoint: Get All Streams
	 *  Gets the list of live streams a user follows based on the OAuth token provided.
	 * Requires Scope: none
	 */
	public Optional<List<Stream>> getAll() {
		// Filter Possibilities

		// ?game=Overwatch
		// channel
		// language
		// stream_type

		// REST Request
		try {
			String requestUrl = String.format("%s/streams/", getTwitchClient().getTwitchEndpoint());
			StreamList responseObject = getTwitchClient().getRestClient().getRestTemplate().getForObject(requestUrl, StreamList.class);

			return Optional.ofNullable(responseObject.getStreams());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	/**
	 * Endpoint: Get Followed Streams
	 *  Gets the list of online streams a user follows based on the OAuth token provided.
	 * Requires Scope: user_read
	 */
	public Optional<List<Stream>> getFollowed() {
		// REST Request
		try {
			String requestUrl = String.format("%s/streams/followed", getTwitchClient().getTwitchEndpoint());
			StreamList responseObject = getTwitchClient().getRestClient().getRestTemplate().getForObject(requestUrl, StreamList.class);

			return Optional.ofNullable(responseObject.getStreams());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	/**
	 * Endpoint: Get Featured Streams
	 *  Gets a list of all featured live streams.
	 * Requires Scope: none
	 */
	public Optional<List<StreamFeatured>> getFeatured() {
		// REST Request
		try {
			String requestUrl = String.format("%s/streams/featured", getTwitchClient().getTwitchEndpoint());
			StreamFeaturedList responseObject = getTwitchClient().getRestClient().getRestTemplate().getForObject(requestUrl, StreamFeaturedList.class);

			return Optional.ofNullable(responseObject.getFeatured());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	/**
	 * Endpoint: Get Streams Summary
	 *  Gets a summary of all live streams.
	 * Requires Scope: none
	 */
	public Optional<StreamSummary> getSummary(Game game) {
		// Build Parameters
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		if(game != null) {
			parameters.add("game", game.getId().toString());
		}

		// REST Request
		try {
			String requestUrl = String.format("%s/streams/summary", getTwitchClient().getTwitchEndpoint());
			UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(requestUrl).queryParams(parameters).build();
			StreamSummary responseObject = getTwitchClient().getRestClient().getRestTemplate().getForObject(uriComponents.toUriString(), StreamSummary.class);

			return Optional.ofNullable(responseObject);
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	public Optional<StreamSummary> getSummary() {
		return this.getSummary(null);
	}
}
