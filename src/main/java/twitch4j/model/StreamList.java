package twitch4j.model;

import java.util.List;
import lombok.Data;

/**
 * Model representing streams.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class StreamList {
	/**
	 * Data
	 */
	private List<Stream> streams;

}
