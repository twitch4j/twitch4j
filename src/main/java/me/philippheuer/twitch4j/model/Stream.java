package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.util.Calendar;
import lombok.Data;

/**
 * Model representing a stream.
 * <p>
 * A stream is a channel, that is currently streaming live.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class Stream {

	@JsonProperty("_id")
	private long id;

	private String game;

	private int viewers;

	private Calendar createdAt;

	private int videoHeight;

	private double averageFps;

	private boolean isPlaylist;

	private TwitchImages preview;

	private Channel channel;

	/**
	 * Gets the stream uptime based on the start date.
	 *
	 * @return The stream uptime.
	 */
	public Duration getUptime() {
		Duration uptime = Duration.between(getCreatedAt().toInstant(), Calendar.getInstance().toInstant());

		return uptime;
	}

}
