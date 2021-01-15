package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenCollectionMetadata {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("created_at")
    private Instant createdAtInstant;

    @JsonProperty("items_count")
    private Integer itemsCount;

    @JsonProperty("owner")
    private KrakenUser owner;

    @JsonProperty("thumbnails")
    private Thumbnail thumbnail;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Thumbnail {
        private String large;
        private String medium;
        private String small;
        private String template;
    }

    @JsonProperty("title")
    private String title;

    @JsonProperty("total_duration")
    private Integer totalDuration;

    @JsonProperty("updated_at")
    private Instant updatedAtInstant;

    @JsonProperty("views")
    private Integer views;
}
