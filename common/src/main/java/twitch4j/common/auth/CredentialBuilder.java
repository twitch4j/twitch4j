package twitch4j.common.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Mono;
import twitch4j.common.json.auth.ValidAccess;

@Setter
@Accessors(fluent = true, chain = true)
class CredentialBuilder implements ICredential.Builder {
	private String accessToken;
	private String refreshToken;

	@Override
	public ICredential build(AuthService service) {
		Mono<ValidAccess> validAccesss = service.validate(accessToken);
		return validAccesss.flatMap(access ->
				Mono.just(new Credential(accessToken,
						refreshToken,
						Instant.now().plus(60, ChronoUnit.DAYS),
						new LinkedHashSet<>(access.getScopes()),
						access.getLogin(),
						access.getUserId())))
				.block();
	}
}
