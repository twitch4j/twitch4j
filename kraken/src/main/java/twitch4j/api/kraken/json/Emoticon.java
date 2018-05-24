package twitch4j.api.kraken.json;

import lombok.Data;

/**
 * Model representing a twitch emote.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class Emoticon {
	private Long id;
 	private Long emoticonSet;
	private String code;
}
