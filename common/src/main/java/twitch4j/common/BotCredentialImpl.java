package twitch4j.common;

import java.time.Instant;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Scope;

@Data
@Accessors(fluent = true)
public class BotCredentialImpl implements IBotCredential {
	private final String accessToken;
	private final String refreshToken;
	private final Instant expiredAt;
	private final Set<Scope> scopes;
	private final String username;
	private final Long userId;

	@Accessors(fluent = false)
	private final boolean knownBot;

	@Accessors(fluent = false)
	private final boolean verified;

	public BotCredentialImpl(ICredential credential, boolean knownBot, boolean verified) {
		this.accessToken = credential.accessToken();
		this.refreshToken = credential.refreshToken();
		this.expiredAt = credential.expiredAt();
		this.scopes = credential.scopes();
		this.username = credential.username();
		this.userId = credential.userId();

		this.knownBot = knownBot;
		this.verified = verified;
	}
}
