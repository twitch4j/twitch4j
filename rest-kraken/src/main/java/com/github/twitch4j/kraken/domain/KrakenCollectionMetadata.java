package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KrakenCollectionMetadata {

    @JsonProperty("_id")
    private String id;
    private Instant createdAt;
    private Integer itemsCount;
    private KrakenUser owner;
    private KrakenCollectionThumbnails thumbnails;
    private String title;
    private Integer totalDuration;
    private Instant updatedAt;
    private Integer views;
}
