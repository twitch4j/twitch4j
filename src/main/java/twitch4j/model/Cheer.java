package twitch4j.model;

import lombok.Data;

/**
 * Model representing a cheer.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class Cheer {

	private String message;

	private User user;

	private Integer bits;
}
