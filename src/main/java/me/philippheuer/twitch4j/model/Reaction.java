package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * Model representing a reaction.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reaction {
	/**
	 * Amount of people that have reacted like this
	 */
	private Long count;

	/**
	 * The emote, that was reacted with.
	 */
	private String emote;

	/**
	 * The users, that reacted like this
	 */
	private List<Long> userIds;
}
