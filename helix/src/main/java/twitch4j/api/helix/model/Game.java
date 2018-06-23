package twitch4j.api.helix.model;

import lombok.Data;

@Data
public class Game {
	/**
	 * The Game Id
	 */
	private Long id;

	/**
	 * The name of the game
	 */
	private String name;

	/**
	 * A url to the box image, contains {width} and {height} as replaceable variables
	 */
	private String boxArtUrl;
}
