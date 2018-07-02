package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * Model representing a team.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class Team {

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
}
