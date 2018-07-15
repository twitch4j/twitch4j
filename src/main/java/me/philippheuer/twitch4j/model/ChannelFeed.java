package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * Model representing a Channel Feed.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class ChannelFeed {

	/**
	 * Cursor
	 */
	@JsonProperty("_cursor")
	private String cursor;

	/**
	 * PubSub Topic
	 */
	@JsonProperty("_topic")
	private String topic;

	/**
	 * PubSub Topic
	 */
	@JsonProperty("_disabled")
	private Boolean disabled;

	/**
	 * Posts
	 */
	private List<ChannelFeedPost> posts;
}
