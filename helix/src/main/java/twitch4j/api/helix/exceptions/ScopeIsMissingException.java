package twitch4j.api.helix.exceptions;

import lombok.Getter;
import twitch4j.common.auth.Scope;

public class ScopeIsMissingException extends RuntimeException {
	@Getter
	private final Scope scope;
	public ScopeIsMissingException(Scope scope) {
		super("Scope is required: " + scope.toString());
		this.scope = scope;
	}
}
