package me.philippheuer.twitch4j.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScopeMissingException extends RuntimeException {

	private String scope;

	public ScopeMissingException(String scope) {
		super(String.format("Access to Scope %s denied!", scope));
		this.scope = scope;
	}
}
