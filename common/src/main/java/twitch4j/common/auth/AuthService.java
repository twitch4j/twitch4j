package twitch4j.common.auth;

import io.reactivex.Single;
import lombok.AllArgsConstructor;
import okhttp3.Headers;
import okhttp3.internal.http.HttpHeaders;
import twitch4j.common.auth.auth.json.ValidAccess;
import twitch4j.common.route.Route;

import java.util.Collections;

@AllArgsConstructor
public class AuthService {
	private final Route route = Route.create("https://id.twitch.tv/oauth2");
	private final Manager manager;

	public Single<Boolean> validate(ICredential credential) {
		Headers headers = Headers.of(Collections.singletonMap("Authorization", "OAuth " + credential.accessToken()));
		return route.get("/validate")
				.headers(headers)
				.exchange(ValidAccess.class);
	}

	public Single<ICredential> create() {

	}

	public Single<ICredential> refresh(ICredential credential) {

	}

	public Single<Boolean> revoke(ICredential credential) {

	}
}
