package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

/**
 * Model representing User objects listed in the users array
 * in the KrakenTeam model
 */
@Data
public class KrakenTeamUser {

    @JsonProperty("_id")
    private long id;

    private String broadcasterLanguage;

    @JsonProperty("created_at")
    private Instant createdAtInstant;

    private String displayName;

    private long followers;

    private String game;

    private String language;

    private String logo;

    private Boolean mature;

    private String name;

    private Boolean partner;

    private String profileBanner;

    private Object profileBannerBackgroundColor;

    private String status;

    @JsonProperty("updated_at")
    private Instant updatedAtInstant;

    private String url;

    private Object videoBanner;

    private long views;

    /**
     * @return creation timestamp
     * @deprecated in favor of getCreatedAtInstant()
     */
    @JsonIgnore
    @Deprecated
    public Date getCreatedAt() {
        return Date.from(createdAtInstant);
    }

    /**
     * @return updated timestamp
     * @deprecated in favor of getUpdatedAtInstant()
     */
    @JsonIgnore
    @Deprecated
    public Date getUpdatedAt() {
        return Date.from(updatedAtInstant);
    }
}
