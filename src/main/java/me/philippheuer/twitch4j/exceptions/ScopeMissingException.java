package me.philippheuer.twitch4j.exceptions;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.enums.Scope;

@Getter
@Setter
public class ScopeMissingException extends RuntimeException {

	private Scope scope;

	public ScopeMissingException(Scope scope) {
		super(String.format("Scope %s is required!", scope.toString()));
		this.scope = scope;
	}
}
