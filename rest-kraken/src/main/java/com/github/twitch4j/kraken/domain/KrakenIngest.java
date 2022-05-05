package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Model representing a twitch ingest server.
 * <p>
 * The Twitch ingesting system is the first stop for a broadcast stream.
 * An ingest server receives your stream, and the ingesting system
 * authorizes and registers streams, then prepares them for viewers.
 * @deprecated Kraken is deprecated and has been shut down on <b>Febuary 28, 2022</b>.
 *             More details about the deprecation are available <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>.
 */
@Data
@Deprecated
public class KrakenIngest {

	@JsonProperty("_id")
	private Long id;

	private String name;

	private Integer availability;

	@JsonProperty("default")
	private Boolean isDefault;

	private String urlTemplate;
}
