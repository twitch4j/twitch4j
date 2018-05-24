package twitch4j.api.kraken.json.unofficial;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
