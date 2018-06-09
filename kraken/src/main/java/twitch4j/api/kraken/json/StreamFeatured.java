package twitch4j.api.kraken.json;

import lombok.Data;

/**
 * Model representing a featured stream.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class StreamFeatured {

	private String text;

	private String image;

	private boolean sponsored;

	private int priority;

	private boolean scheduled;

	private Stream stream;

}
