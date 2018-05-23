package twitch4j.api.helix.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
abstract class AbstractHelixOperation {
	private final OkHttpClient httpClient;
	private final ObjectMapper mapper;
	private final String clientId;
}
