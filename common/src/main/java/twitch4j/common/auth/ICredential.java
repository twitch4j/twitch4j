package twitch4j.common.auth;

import java.time.Instant;
import java.util.Set;

public interface ICredential {
	static Builder builder() {
		return new CredentialBuilder();
	}

	String accessToken();

	String refreshToken();

	Instant expiredAt();

	Set<Scope> scopes();

	String username();

	Long userId();

	interface Builder {
		Builder accessToken(String accessToken);

		Builder refreshToken(String refreshToken);

		ICredential build(AuthService service);
	}
}
