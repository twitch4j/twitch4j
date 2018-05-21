package twitch4j.api.kraken.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;

@RequiredArgsConstructor
public abstract class AbstractEndpoint {
	private final OkHttpClient httpClient;
	private final ObjectMapper mapper;
}
