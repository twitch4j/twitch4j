package twitch4j.model;

import lombok.Data;

/**
 * Model representing a twitch emote.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class Emote {

	private Long id;

	private String code;

}
