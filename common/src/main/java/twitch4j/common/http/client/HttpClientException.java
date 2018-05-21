package twitch4j.common.http.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.Headers;
import twitch4j.common.json.ErrorResponse;

@Data
@EqualsAndHashCode(callSuper = false)
public class HttpClientException extends RuntimeException {
	private final HttpResponseStatus httpResponseStatus;
	private final Headers headers;
	private final ErrorResponse errorResponse;
}
