package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Abstract base for result lists.
 * @deprecated Due endpoints is deprecated decommission have been planned on <b>Febuary 28, 2022</b>.
 *             More details about decommission finds <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>
 */
@Data
@Deprecated
public abstract class AbstractResultList {

	/**
	 * Cursor
	 */
	@JsonProperty("_cursor")
	private String cursor;

	/**
	 * Total Entries
	 */
	@JsonProperty("_total")
	private Long total;

}
