package twitch4j.api;

import lombok.RequiredArgsConstructor;
import twitch4j.api.kraken.operations.UserOperation;
import twitch4j.common.http.client.HttpClient;

@Deprecated
@RequiredArgsConstructor
public class TwitchKraken {
	private final HttpClient httpClient;

	public UserOperation userOperation() {
		return new UserOperation(httpClient, mapper);
	}
}
