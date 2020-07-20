package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenVideo {
    @JsonProperty("_id")
    private String id;
    private String title;
    private String description;
    private String descriptionHtml;
    private Integer broadcastId;
    private String broadcastType;
    private String status;
    private String tagList;
    private Long views;
    private String url;
    private String language;
    private Instant createdAt;
    private String viewable;
    private Instant viewableAt;
    private Instant publishedAt;
    private Instant recordedAt;
    private String game;
    private Long length;
    private Preview preview;
    private String animatedPreviewUrl;
    private Thumbnails thumbnails;
    private Map<String, Double> fps;
    private Map<String, String> resolutions;
    private Channel channel;

    public String getIdForApiCalls() {
        // The id returned in this object has a `v` prefix, which is not desirable for further api calls
        return this.id.charAt(0) == 'v' ? id.substring(1) : id;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Preview {
        private String small;
        private String medium;
        private String large;
        private String template;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Thumbnail {
        private String type;
        private String url;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Thumbnails {
        private List<Thumbnail> small;
        private List<Thumbnail> medium;
        private List<Thumbnail> large;
        private List<Thumbnail> template;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Channel {
        @JsonProperty("_id")
        private String id;
        private String name;
        private String displayName;
    }
}
