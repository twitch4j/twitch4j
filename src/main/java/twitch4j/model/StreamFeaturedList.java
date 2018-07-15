package twitch4j.model;

import java.util.List;
import lombok.Data;

/**
 * Model representing featured streams.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class StreamFeaturedList {
	/**
	 * Data
	 */
	private List<StreamFeatured> featured;

}
