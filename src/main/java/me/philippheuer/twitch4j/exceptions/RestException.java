package me.philippheuer.twitch4j.exceptions;

import lombok.Getter;
import lombok.Setter;

import me.philippheuer.twitch4j.model.Error;

@Getter
@Setter
public class RestException extends RuntimeException {

	private Error restError;

	public RestException(Error restError) {
		super();
		setRestError(restError);
	}
}
