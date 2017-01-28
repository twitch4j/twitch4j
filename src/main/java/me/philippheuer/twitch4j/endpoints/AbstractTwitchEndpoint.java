package me.philippheuer.twitch4j.endpoints;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

@Getter
@Setter
public class AbstractTwitchEndpoint {

	/**
	 * Cache - Objects
	 */
	static protected Map<String, Object> restObjectCache = ExpiringMap.builder()
			.expiration(1, TimeUnit.MINUTES)
			.expirationPolicy(ExpirationPolicy.ACCESSED)
			.build();

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractTwitchEndpoint.class);

	/**
	 * Holds the API Instance
	 */
	private TwitchClient twitchClient;

	/**
	 * AbstractTwitchEndpoint
	 * @TODO: Description
	 * @param twitchClient
	 */
	public AbstractTwitchEndpoint(TwitchClient twitchClient) {
		// Properties
		setTwitchClient(twitchClient);
	}

	public Logger getLogger() {
		return logger;
	}
}
