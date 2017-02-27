package me.philippheuer.twitch4j.model.tmi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * Model representing the chatters of a channel.
 * <p>
 * The model contains a list of moderators, staff, admins, global moderators and viewers.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Chatter {

	/**
	 * The present Moderators.
	 */
	private List<String> moderators;

	/**
	 * The present Twitch Staff.
	 */
	private List<String> staff;

	/**
	 * The present Twitch Admins.
	 */
	private List<String> admins;

	/**
	 * The present global Moderators.
	 */
	private List<String> globalMods;

	/**
	 * The present viewers.
	 */
	private List<String> viewers;

}
