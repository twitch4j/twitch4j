package twitch4j.common.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
class AuthConfig {
	private final String clientId;
	private final String clientSecret;
	private final String redirectUri;
	private final boolean forceVerify;
	private final Set<Scope> defaultScopes;

	static AuthConfig of(Manager.Builder builder) {
		return new AuthConfig(
				builder.clientId(),
				builder.clientSecret(),
				builder.redirectUri(),
				builder.forceVerify(),
				builder.defaultScopes()
		);
	}
}
