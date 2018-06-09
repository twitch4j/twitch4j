package twitch4j.api.kraken.json.unofficial;

import lombok.Data;

/**
 * Model representing a the ember response.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class Ember {

	/**
	 * Name of the primary team
	 */
	private String primaryTeamName;

	/**
	 * Display name of the primary team
	 */
	private String primaryTeamDisplayName;
}
