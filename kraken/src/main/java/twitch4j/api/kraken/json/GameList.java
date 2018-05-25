package twitch4j.api.kraken.json;

import java.util.List;
import lombok.Data;

/**
 * Model representing a list of games.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class GameList {
	/**
	 * Data
	 */
	private List<Game> games;

}
