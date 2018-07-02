package me.philippheuer.twitch4j.model;

import lombok.Data;

/**
 * Model representing the images twitch returns.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class TwitchImages {

	private String small;

	private String medium;

	private String large;

	private String template;
}
