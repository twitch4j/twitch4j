package twitch4j.model;

import lombok.Data;

/**
 * Model representing a oauth token.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class Token {

	private Boolean valid = false;

	private String userName;

	private Long userId;

	private String clientId;

	private TokenAuthorization authorization;
}
