package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * Model representing a Channel Feed.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
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
