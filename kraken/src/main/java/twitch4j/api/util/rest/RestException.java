package twitch4j.api.util.rest;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import twitch4j.common.json.ErrorResponse;

@Getter
@AllArgsConstructor
public class RestException extends IOException {

	private final ErrorResponse errorResponse;

	@Override
	public String toString() {
		String message = (errorResponse.getMessage().equals("") || errorResponse.getMessage() == null) ? errorResponse.getError() : errorResponse.getMessage();
		return getClass().getSimpleName()
				+ " ["
				+ "code=" + errorResponse.getStatus() + ", "
				+ "message=\"" + message + "\""
				+ "]";
	}
}
