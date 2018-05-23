package twitch4j.common.json.auth;

import lombok.Data;
import twitch4j.common.auth.Scope;

import java.util.Set;

@Data
public class AccessToken {
	private final String accessToken;
	private final String refreshToken;
	private final Long expiresIn;
	private final Set<Scope> scope;
}
