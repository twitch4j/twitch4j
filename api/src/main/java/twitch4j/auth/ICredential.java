package twitch4j.auth;

import twitch4j.api.kraken.json.User;

import java.time.Instant;
import java.util.Set;

public interface ICredential {
	String accessToken();
	String refreshToken();
	Instant expiredAt();
	Set<Scope> scopes();
	User user();
}
