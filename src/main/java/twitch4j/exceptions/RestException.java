package twitch4j.exceptions;

import lombok.Getter;
import lombok.Setter;
import twitch4j.model.RestError;

@Getter
@Setter
public class RestException extends RuntimeException {

	private RestError restError;

	public RestException(RestError restError) {
		super();
		setRestError(restError);
	}
}
