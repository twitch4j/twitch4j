package me.philippheuer.twitch4j.streamlabs.endpoints;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class AbstractStreamlabsEndpoint {

	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(AbstractStreamlabsEndpoint.class);

	/**
	 * Holds the API Instance
	 */
	private StreamlabsClient streamlabsClient;

	/**
	 * AbstractTwitchEndpoint
	 * @param streamlabsClient
	 */
	public AbstractStreamlabsEndpoint(StreamlabsClient streamlabsClient) {
		// Properties
		setStreamlabsClient(streamlabsClient);
	}
}
