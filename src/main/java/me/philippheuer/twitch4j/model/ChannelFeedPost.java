package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Calendar;

/**
 * Model representing a Channel Feed Post.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 * @see User
 * @see ReactionList
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelFeedPost {

	/**
	 * Body Text
	 */
	private String body;

	/**
	 * Created At
	 */
	private Calendar createdAt;

	/**
	 * Was deleted?
	 */
	private Boolean deleted;

	/**
	 * Post Id
	 */
	private String id;

	/**
	 * Reactions
	 */
	private ReactionList reactions;

	/**
	 * Posting User
	 */
	private User user;

}
