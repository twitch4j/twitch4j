package twitch4j.api.kraken.model;

import lombok.Data;

/**
 * Model representing a popular game on twitch.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class TopGame {

	private Game game;

	private int viewers;

	private int channels;
}
