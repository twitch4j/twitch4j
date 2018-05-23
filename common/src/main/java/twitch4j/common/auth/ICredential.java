package twitch4j.common.auth;

import java.time.Instant;
import java.util.Set;

public interface ICredential {
	String accessToken();
	String refreshToken();
	Instant expiredAt();
	Set<Scope> scopes();
	String username();
	Long userId();

	static Builder builder() {
		return new CredentialBuilder();
	}

	interface Builder {
		Builder accessToken(String accessToken);
		Builder refreshToken(String refreshToken);

		ICredential build(AuthService service);
	}
}
