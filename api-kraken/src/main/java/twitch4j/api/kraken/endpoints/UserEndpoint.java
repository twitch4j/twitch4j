package twitch4j.api.kraken.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

public class UserEndpoint extends AbstractEndpoint {
	public UserEndpoint(OkHttpClient httpClient, ObjectMapper mapper) {
		super(httpClient, mapper);
	}
}
