package twitch4j.api.kraken.json;

import lombok.Data;

/**
 * Model representing the global twitch stream summary.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class StreamSummary {

	/**
	 * Total amount of streams that are live.
	 */
	private Integer channels;

	/**
	 * Total amount of viewers watcing over all channels.
	 */
	private Integer viewers;

}
