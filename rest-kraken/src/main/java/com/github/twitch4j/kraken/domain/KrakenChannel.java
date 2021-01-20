package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class KrakenChannel {
    @JsonProperty("_id")
    private String id;
    private String broadcasterLanguage;
    private String broadcasterType;
    private Instant createdAt;
    private String displayName;
    private String email;
    private Long followers;
    private String game;
    private String language;
    private String logo;
    private Boolean mature;
    private String name;
    private Boolean partner;
    private String profileBanner;
    private String profileBannerBackgroundColor;
    private String status;
    private String streamKey;
    private Instant updatedAt;
    private String url;
    private String videoBanner;
    private Long views;
}
