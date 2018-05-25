package twitch4j.common.json;

import lombok.Data;

/**
 * Model representing an error within the http api.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class ErrorResponse {
	private String error;
	private Integer status;
	private String message;
}
