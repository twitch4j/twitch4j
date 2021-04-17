package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
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
