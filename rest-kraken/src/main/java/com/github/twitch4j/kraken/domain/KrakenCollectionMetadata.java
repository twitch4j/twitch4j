package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenCollectionMetadata {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("items_count")
    private Integer itemsCount;

    private KrakenUser owner;

    private KrakenCollectionThumbnails thumbnails;

    private String title;

    @JsonProperty("total_duration")
    private Integer totalDuration;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    private Integer views;
}
