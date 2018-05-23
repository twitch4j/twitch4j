package twitch4j.api.kraken.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public abstract class AbstractEndpoint {
	private final RestTemplate restTemplate;
	private final ObjectMapper mapper;
}
