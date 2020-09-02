package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Model representing teams.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenTeamList {
	/**
	 * Data
	 */
	private List<KrakenTeam> teams;

}
