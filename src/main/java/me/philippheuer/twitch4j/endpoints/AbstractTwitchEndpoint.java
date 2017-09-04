package me.philippheuer.twitch4j.endpoints;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.exceptions.ScopeMissingException;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class AbstractTwitchEndpoint {

	/**
	 * Cache - Objects
	 */
	static protected ExpiringMap<String, Object> restObjectCache = ExpiringMap.builder()
			.expiration(30, TimeUnit.SECONDS)
			.expirationPolicy(ExpirationPolicy.ACCESSED)
			.variableExpiration()
			.build();

	/**
	 * Holds the API Instance
	 */
	private TwitchClient twitchClient;

	/**
	 * AbstractTwitchEndpoint
	 *
	 * @param twitchClient todo
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
	protected void checkScopePermission(Set<String> scopes, Set<String> requiredScopes) {
		for (String requiredScope : requiredScopes) {
			if (!scopes.contains(requiredScope)) {
				throw new ScopeMissingException(requiredScope);
			}
		}
	}

}
