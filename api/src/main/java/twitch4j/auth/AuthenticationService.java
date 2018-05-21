package twitch4j.auth;

public interface AuthenticationService {
	default void authorize() {
		authorize(false);
	}
	void authorize(boolean force);
}
