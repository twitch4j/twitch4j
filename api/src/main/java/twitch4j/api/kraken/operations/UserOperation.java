package twitch4j.api.kraken.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import twitch4j.api.kraken.json.User;
import twitch4j.auth.ICredential;
import twitch4j.rest.HttpMethod;

import java.io.IOException;
import java.util.Optional;

public class UserOperation extends AbstractKrakenOperation {

	public UserOperation(OkHttpClient httpClient, ObjectMapper mapper) {
		super(httpClient, mapper);
	}

	public User getUser(long id) {

	}

	public Optional<User> getUser(String name) {

	}

	public User getUser(ICredential credential) {
		if (credential.user() == null) {
			try (Response response = getHttpClient().newCall(buildRequest(HttpMethod.GET, "", null, singletonHeaderAuthorization(credential.accessToken())))
					.execute()) {
				return getMapper().readValue(response.body().bytes(), User.class);
			} catch (IOException e) {
				throw e;
			}
		} else return credential.user();
	}
}
