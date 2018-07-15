package twitch4j.model;

import java.util.List;
import lombok.Data;

/**
 * Model representing a popular games on twitch.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class TopGameList {

	private int total;

	private List<TopGame> top;
}
