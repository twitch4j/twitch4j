package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenCollection {

    @JsonProperty("_id")
    public String id;

    @JsonProperty("items")
    public List<Video> items;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Video {
        @JsonProperty("_id")
        private String id;

        @JsonProperty("description_html")
        private String descriptionHtml;

        private Integer duration;
        private String game;

        @JsonProperty("item_id")
        private String itemId;

        @JsonProperty("item_type")
        private String itemType;
        private KrakenUser owner;

        @JsonProperty("published_at")
        private Instant publishedAtInstant;

        private Thumbnails thumbnails;
        private String title;
        private Integer views;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Thumbnails {
        private String large;
        private String medium;
        private String small;
        private String template;
    }

}
