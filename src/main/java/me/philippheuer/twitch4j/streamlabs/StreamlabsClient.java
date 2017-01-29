package me.philippheuer.twitch4j.streamlabs;

import me.philippheuer.twitch4j.helper.HeaderRequestInterceptor;
import me.philippheuer.twitch4j.helper.QueryRequestInterceptor;
import me.philippheuer.twitch4j.helper.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.*;

import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.pubsub.TwitchPubSub;
import org.springframework.social.support.HttpRequestDecorator;
import org.springframework.util.Assert;

import java.io.File;

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
	public StreamlabsClient(String clientId, String clientSecret) {
		super();

		setClientId(clientId);
		setClientSecret(clientSecret);

		// Initialize REST Client
		getRestClient().putRestInterceptor(new QueryRequestInterceptor("access_token", getClientId()));
	}

	/**
	 * Client Builder
	 */
	@Builder(builderMethodName = "builder")
	public static StreamlabsClient streamlabsClientBuilder(String clientId, String clientSecret) {
		// Reqired Parameters
		Assert.notNull(clientId, "You need to provide a client id!");
		Assert.notNull(clientSecret, "You need to provide a client secret!");

		// Initalize instance
		final StreamlabsClient streamlabsClient = new StreamlabsClient(clientId, clientSecret);

		// Return builded instance
		return streamlabsClient;
	}

	/**
	 * Get the full api endpoint address.
	 * @return The full api endpoint url.
	 */
	public String getEndpointUrl() {
		return String.format("%s/%s", getStreamlabsEndpoint(), getStreamlabsEndpointVersion());
	}
}
