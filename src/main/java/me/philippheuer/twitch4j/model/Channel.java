package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Data;

/**
 * Model representing a twitch channel.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class Channel {

	@JsonProperty("_id")
	private Long id;

	private String broadcasterLanguage;

	private Date createdAt;

	private String displayName;

	private String email;

	private Long followers;

	private String game;

	private String language;

	private String logo;

	private boolean mature;

	private String name;

	private boolean partner;

	private String profileBanner;

	private String profileBannerBackgroundColor;

	private String status;

	private String streamKey;

	private Date updatedAt;

	private String url;

	private String videoBanner;

	private Long views;
}
