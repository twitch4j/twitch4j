package twitch4j.api;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;

@RequiredArgsConstructor
public class TwitchHelix {
	private static final String BASE_URL = "";

	private final OkHttpClient httpClient;
	private final String clientId;
}
