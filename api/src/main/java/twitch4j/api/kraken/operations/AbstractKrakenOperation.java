package twitch4j.api.kraken.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import twitch4j.rest.HttpMethod;

import java.util.Collections;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
abstract class AbstractKrakenOperation {
	private static final HttpUrl BASE_URL = HttpUrl.parse("https://api.twitch.tv/kraken");

	private final OkHttpClient httpClient;
	private final ObjectMapper mapper;

	Request buildRequest(HttpMethod method, String uri, RequestBody body, Headers headers) {
		return new Request.Builder()
				.method(method.name(), body)
				.url(BASE_URL.newBuilder().addPathSegments(uri).build())
				.headers(headers)
				.build();
	}

	Headers singletonHeaderAuthorization(String accessToken) {
		return Headers.of(Collections.singletonMap("Authorization", "OAuth " + accessToken));
	}
}
