package twitch4j.api.kraken.json;

import lombok.Data;

/**
 * Model representing a response with a token.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class TokenResponse {

	private Token token;

}
