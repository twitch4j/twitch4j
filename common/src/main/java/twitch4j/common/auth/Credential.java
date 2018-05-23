package twitch4j.common.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@Accessors(fluent = true)
class Credential implements ICredential {
	private final String accessToken;
	private final String refreshToken;
	private final Instant expiredAt;
	private final Set<Scope> scopes;
	private final String username;
	private final Long userId;

	@Override
	public String toString() {
		return new StringBuilder("ICredential[")
				.append("access_token=").append((accessToken != null) ? "\"****\"" : "null").append(", ")
				.append("refresh_token=").append((refreshToken != null) ? "\"****\"" : "null").append(", ")
				.append("expired_at=").append(expiredAt.toString()).append(", ")
				.append("scopes=").append(scopes.toString()).append(", ")
				.append("user_id=").append(userId.toString()).append(", ")
				.append("user_name=\"").append((accessToken != null) ? "\"" + username +"\"" : "null").append("\"")
				.append("]").toString();
	}
}
