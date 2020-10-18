package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Model representing a list of ingest servers.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenIngestList {
	/**
	 * Data
	 */
	private List<KrakenIngest> ingests;

}
