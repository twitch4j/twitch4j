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
public class KrakenCollectionItem {

    @JsonProperty("_id")
    public String id;
    public String descriptionHtml;
    public Integer duration;
    public String game;
    public String itemId;
    public String itemType;
    public KrakenUser owner;
    public Instant publishedAt;
    public KrakenCollectionThumbnails thumbnails;
    public String title;
    public Integer views;
}
