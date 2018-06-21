package twitch4j.api.kraken.model;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * Model representing a sets of twitch emotes.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class EmoteSets {
	private Map<String, List<Emote>> emoticonSets;

}
