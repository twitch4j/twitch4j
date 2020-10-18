package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenClip {
    private String slug;
    private String trackingId;
    private String url;
    private String embedUrl;
    private String embedHtml;
    private SimpleUser broadcaster;
    private SimpleUser curator;
    private VideoOnDemand vod;
    private String game;
    private String language;
    private String title;
    private Long views;
    private Double duration;
    private Instant createdAt;
    private Thumbnail thumbnails;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SimpleUser {
        private String id;
        private String name;
        private String displayName;
        private String channelUrl;
        private String logo;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VideoOnDemand {
        private String id;
        private String url;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Thumbnail {
        private String medium;
        private String small;
        private String tiny;
    }
}
