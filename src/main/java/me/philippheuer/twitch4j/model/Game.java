package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * Model representing a game.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {

	@JsonProperty("_id")
	private Long id;

	private String name;

	private GameBox box;

	private GameLogo logo;

	private long giantbombId;

	private int popularity; // From search results

	@Data
	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	private class GameBox {
		private String large;
		private String medium;
		private String small;
		private String template;
	}

	@Data
	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	private class GameLogo {
		private String large;
		private String medium;
		private String small;
		private String template;
	}
}
