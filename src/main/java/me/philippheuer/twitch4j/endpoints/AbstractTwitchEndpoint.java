package me.philippheuer.twitch4j.endpoints;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.philippheuer.twitch4j.exceptions.ScopeMissingException;
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
	protected static final Logger logger = LoggerFactory.getLogger(AbstractTwitchEndpoint.class);

	/**
	 * Holds the API Instance
	 */
	private TwitchClient twitchClient;

	/**
	 * AbstractTwitchEndpoint
	 *
	 * @param twitchClient
	 * TODO: Description
	 */
	public AbstractTwitchEndpoint(TwitchClient twitchClient) {
		// Properties
		setTwitchClient(twitchClient);
	}

	/**
	 * Check that the api has the required scopes before making a request
	 *
	 * @param scopes         Scopes, we have access to.
	 * @param requiredScopes Scopes, we want to access.
	 */
	protected void checkScopePermission(List<String> scopes, List<String> requiredScopes) {
		for (String requiredScope : requiredScopes) {
			if (!scopes.contains(requiredScope)) {
				throw new ScopeMissingException(requiredScope);
			}
		}
	}

}
