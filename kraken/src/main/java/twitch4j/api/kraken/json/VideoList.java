package twitch4j.api.kraken.json;

import java.util.List;
import lombok.Data;

/**
 * Model representing videos.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class VideoList {
	/**
	 * Data
	 */
	private List<Video> videos;

}
