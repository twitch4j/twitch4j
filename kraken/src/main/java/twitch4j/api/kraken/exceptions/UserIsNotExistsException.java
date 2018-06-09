package twitch4j.api.kraken.exceptions;

import lombok.Getter;

@Getter
public class UserIsNotExistsException extends RuntimeException {
	private final String username;

	public UserIsNotExistsException(String username) {
		super("Username: \"" + username + "\" it doesn't exist");
		this.username = username;
	}
}
