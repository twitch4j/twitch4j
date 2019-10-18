package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * Model representing User objects listed in the users array
 * in the KrakenTeam model
 */
@Data
public class KrakenTeamUser {

    @JsonProperty("_id")
    private long id;

    @JsonProperty("broadcaster_language")
    private String broadcasterLanguage;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("display_name")
    private String displayName;

    private long followers;

    private String game;

    private String language;

    private String logo;

    private Boolean mature;

    private String name;

    private Boolean partner;

    @JsonProperty("profile_banner")
    private String profileBanner;

    @JsonProperty("profile_banner_background_color")
    private Object profileBannerBackgroundColor;

    private String status;

    @JsonProperty("updated_at")
    private Date updatedAt;

    private String url;

    @JsonProperty("video_banner")
    private Object videoBanner;

    private long views;
}
