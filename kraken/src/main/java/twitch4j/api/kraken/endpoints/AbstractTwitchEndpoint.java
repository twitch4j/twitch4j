package twitch4j.api.kraken.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.web.client.RestTemplate;
import twitch4j.api.kraken.exceptions.ScopeMissingException;
import twitch4j.common.auth.Scope;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
	 * @param scopes         Scopes, we have access to.
	 * @param requiredScopes Scopes, we want to access.
	 */
	protected void checkScopePermission(Collection<Scope> scopes, Collection<Scope> requiredScopes) throws ScopeMissingException {
		for (Scope requiredScope : requiredScopes) {
			if (!scopes.contains(requiredScope)) {
				throw new ScopeMissingException(requiredScope);
			}
		}
	}

}
