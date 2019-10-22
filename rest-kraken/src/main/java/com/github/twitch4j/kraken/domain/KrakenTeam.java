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

	@JsonProperty("display_name")
	private String displayName;

	private String info;

	private String logo;

	private String background;

	private String banner;

  @JsonProperty("created_at")
  private Date createdAt;

  @JsonProperty("updated_at")
  private Date updatedAt;

	private List<KrakenTeamUser> users;
}
