package me.philippheuer.twitch4j.model;

import lombok.Data;

import java.util.List;

/**
 * Model representing top videos.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class VideoTopList {
	/**
	 * Data
	 */
	private List<Video> vods;

}
