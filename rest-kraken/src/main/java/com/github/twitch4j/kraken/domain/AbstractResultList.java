package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Abstract base for result lists.
 * @deprecated Kraken is deprecated and has been shut down on <b>Febuary 28, 2022</b>.
 *             More details about the deprecation are available <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>.
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
