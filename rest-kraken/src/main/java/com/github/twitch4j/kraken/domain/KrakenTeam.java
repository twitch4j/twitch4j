package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Model representing a team.
 */
@Data
public class KrakenTeam {

	@JsonProperty("_id")
	private long id;

	private String name;

	private String displayName;

	private String info;

	private String logo;

	private String background;

	private String banner;

	private Date createdAt;

	private Date updatedAt;

	private List<KrakenTeamUser> users;
}
