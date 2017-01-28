package me.philippheuer.twitch4j.streamlabs;

import me.philippheuer.twitch4j.helper.HeaderRequestInterceptor;
import me.philippheuer.twitch4j.helper.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.*;

import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.pubsub.TwitchPubSub;

@Getter
@Setter
public class StreamlabsClient {

	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(TwitchPubSub.class);

	/**
	 * Holds the API Instance
	 */
	private TwitchClient twitchClient;

	/**
	 * Rest Client
	 */
	private RestClient restClient = new RestClient();

	/**
	 * Streamlabs API Endpoint
	 */
	public final String streamlabsEndpoint = "https://www.twitchalerts.com/api";

	/**
	 * Streamlabs API Version
	 */
	public final String streamlabsEndpointVersion = "v1.0";

	/**
	 * Streamlabs Client Id
	 */
	@Singular
	private String clientId;

	/**
	 * Streamlabs Client Secret
	 */
	@Singular
	private String clientSecret;

	/**
	 * Constructor
	 */
	public StreamlabsClient(TwitchClient twitchClient) {
		setTwitchClient(twitchClient);

		// Initialize REST Client
		// getRestClient().putRestInterceptor();
		// getRestClient().putRestInterceptor(new HeaderRequestInterceptor("Client-ID", getClientId()));
	}

	public String getEndpointUrl() {
		return String.format("%s/%s", getStreamlabsEndpoint(), getStreamlabsEndpointVersion());
	}
}
