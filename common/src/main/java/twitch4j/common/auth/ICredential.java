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

	boolean chatBot();

	static Builder builder() {
		return new CredentialBuilder();
	}

	interface Builder {
		Builder accessToken(String accessToken);
		Builder refreshToken(String refreshToken);
		Builder credentialManager(Manager.Builder credentialManager);
		Builder chatBot(boolean chatBot);

		ICredential build(AuthService service);
	}
}
