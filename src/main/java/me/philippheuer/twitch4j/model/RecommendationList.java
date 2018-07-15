package me.philippheuer.twitch4j.model;

import java.util.List;
import lombok.Data;

/**
 * Model representing a list of recommendations.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class RecommendationList {
	/**
	 * Data
	 */
	private List<Recommendation> recommendedStreams;

}
