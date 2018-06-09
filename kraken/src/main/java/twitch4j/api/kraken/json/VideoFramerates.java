package twitch4j.api.kraken.json;

import lombok.Data;

/**
 * Model representing a video framerate.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class VideoFramerates {

	private double audioOnly;

	private double medium;

	private double mobile;

	private double high;

	private double low;

	private double chunked;
}
