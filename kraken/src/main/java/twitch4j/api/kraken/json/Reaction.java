package twitch4j.api.kraken.json;

import java.util.List;
import lombok.Data;

/**
 * Model representing a reaction.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class Reaction {
	/**
	 * Amount of people that have reacted like this
	 */
	private Long count;

	/**
	 * The emote, that was reacted with.
	 */
	private String emote;

	/**
	 * The users, that reacted like this
	 */
	private List<Long> userIds;
}
