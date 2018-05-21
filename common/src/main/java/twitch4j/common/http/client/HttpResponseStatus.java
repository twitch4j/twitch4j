package twitch4j.common.http.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class HttpResponseStatus {
	private final int code;
	private final String message;
}
