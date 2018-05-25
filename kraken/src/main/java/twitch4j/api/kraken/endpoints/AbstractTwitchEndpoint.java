package twitch4j.api.kraken.endpoints;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.web.client.RestTemplate;
import twitch4j.api.kraken.exceptions.ScopeMissingException;
import twitch4j.common.auth.Scope;

@RequiredArgsConstructor
abstract class AbstractTwitchEndpoint {

	/**
	 * Cache - Objects
	 */
	static protected ExpiringMap<String, Object> restObjectCache = ExpiringMap.builder()
			.expiration(30, TimeUnit.SECONDS)
			.expirationPolicy(ExpirationPolicy.ACCESSED)
			.variableExpiration()
			.build();

	/**
	 * Holds the {@link RestTemplate} instance
	 */
	protected final RestTemplate restTemplate;


	/**
	 * Check that the api has the required scopes before making a request
	 *
	 * @param scopes        Scopes, we have access to.
	 * @param requiredScope Scope, we want to access.
	 */
	protected void checkScopePermission(Collection<Scope> scopes, Scope requiredScope) throws ScopeMissingException {
		if (!scopes.contains(requiredScope)) {
			throw new ScopeMissingException(requiredScope);
		}
	}

}
