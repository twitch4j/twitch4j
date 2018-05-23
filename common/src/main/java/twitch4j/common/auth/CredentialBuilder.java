package twitch4j.common.auth;

import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true, chain = true)
class CredentialBuilder implements ICredential.Builder {
	private String accessToken;
	private String refreshToken;

	@Override
	protected ICredential build(AuthService service) {
		return null;
	}
}
