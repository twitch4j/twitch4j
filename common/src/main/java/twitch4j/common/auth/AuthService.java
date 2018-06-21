package twitch4j.common.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import twitch4j.Configuration;
import twitch4j.common.json.auth.AccessToken;
import twitch4j.common.json.auth.ValidAccess;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.route.Route;

@AllArgsConstructor
public class AuthService {
	private final Configuration configuration;
	private final Router router;

	public Mono<String> createRedirectUri(String redirectUri, @Nullable List<Scope> scopes) {
		Objects.requireNonNull(redirectUri, "redirect_uri");

		return Mono.fromSupplier(() -> {
			StringBuilder sb = new StringBuilder(router.getHttpClient().getBaseUrl())
					.append("/authorize")
					.append("?client_id=").append(configuration.getClientId())
					.append("&redirect_uri=").append(redirectUri)
					.append("&response_type=code")
					.append("&scope=").append(Scope.join((scopes == null || scopes.isEmpty()) ? configuration.getDefaultScopes() : scopes));

			return sb.toString();
		});
	}

	Mono<ValidAccess> validate(String accessToken) {
		return Route.get("/validate", ValidAccess.class)
				.newRequest()
				.header("Authorization", "OAuth " + accessToken)
				.exchange(router);
	}

	public Mono<Boolean> validate(ICredential credential) {
		return validate(credential.accessToken())
				.onErrorReturn(null)
				.flatMap(valid -> Mono.just(valid != null));
	}

	public Mono<ICredential> create(String authorizationCode) {
		return Route.post("/token", AccessToken.class)
				.newRequest()
				.query("client_id", configuration.getClientId())
				.query("client_secret", configuration.getClientSecret())
				.query("code", authorizationCode)
				.query("grant_type", "authorization_code")
				.query("redirect_uri", configuration.getRedirectUri())
				.exchange(router)
				.flatMap(this::build);
	}

	private Mono<ICredential> build(AccessToken token) {
		return validate(token.getAccessToken())
				.flatMap(valid -> Mono.just(new Credential(token.getAccessToken(),
						token.getRefreshToken(),
						Instant.now().plus(token.getExpiresIn(), ChronoUnit.SECONDS),
						token.getScope(),
						valid.getLogin(),
						valid.getUserId())));
	}

	public Mono<ICredential> refresh(ICredential credential) {
		return Route.post("/token", AccessToken.class)
				.newRequest()
				.query("grant_type", "refresh_token")
				.query("refresh_token", credential.refreshToken())
				.query("client_id", configuration.getClientId())
				.query("client_secret", configuration.getClientSecret())
				.exchange(router)
				.flatMap(this::build);
	}

	public Mono<Boolean> revoke(ICredential credential) {
		return validate(credential.accessToken())
				.flatMap(access -> Route.post("/revoke", Boolean.class)
						.newRequest()
						.query("client_id", access.getClientId())
						.query("token", credential.accessToken())
						.exchange(router)
						.onErrorReturn(false)
						.thenReturn(true));
	}
}
