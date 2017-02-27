package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;

import java.util.Date;
import java.util.Optional;

/**
 * Model representing a twitch channel.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
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

	// Holds related Twitch Credentials
	private Optional<OAuthCredential> twitchCredential = Optional.empty();

	// Holds related Streamlabs Credentials
	private Optional<OAuthCredential> streamlabsCredential = Optional.empty();
}
