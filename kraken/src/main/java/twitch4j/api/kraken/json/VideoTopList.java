package twitch4j.api.kraken.json;

import java.util.List;
import lombok.Data;

/**
 * Model representing top videos.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class VideoTopList {
	/**
	 * Data
	 */
	private List<Video> vods;

}
