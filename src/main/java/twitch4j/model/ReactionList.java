package twitch4j.model;

import java.util.Map;
import lombok.Data;

/**
 * Model representing a list of games.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class ReactionList {
	/**
	 * Data
	 */
	private Map<String, Reaction> reactions;

}
