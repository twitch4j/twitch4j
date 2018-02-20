package me.philippheuer.twitch4j.api.kraken.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Channel extends KrakenModel {
	@JsonProperty("_id")
	private final Long id;
	@JsonProperty("name")
	private final String username;
	private final String displayName;
	private final String url;

	private final String logo;
	private final Locale language;
	private final String videoBanner;
	private final String profileBanner;
	private final Color profileBannerBackgroundColor;

	private final Game game;
	private final String status;
	private final boolean mature;
	private final Locale broadcasterLanguage;

	private final boolean partner;

	private final Calendar createdAt;
	private final Calendar updatedAt;

	@JsonProperty("followers")
	private final long followersCount;
	@JsonProperty("views")
	private final long viewsCount;

	public User getUser() { return null; }

	public List<User> getSubscribers() { return null; }

//	public ClipCreate createChannelClip() {
//		if (getCredential().isPresent()) {
//			if (getCredential().get().getUser().getId().equals(id)) {
//				// self create clip
//			} else {
//				getCredential().get().getUser().createChannelClip(this);
//			}
//		}
//	}
}
