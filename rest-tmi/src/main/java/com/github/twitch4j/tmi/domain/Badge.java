package com.github.twitch4j.tmi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class Badge {
    @JsonProperty("image_url_1x")
    private String imageUrl1x;
    @JsonProperty("image_url_2x")
    private String imageUrl2x;
    @JsonProperty("image_url_4x")
    private String imageUrl4x;
    private String description;
    private String title;
    private String clickAction;
    private String clickUrl;
    private Instant lastUpdated;
}
