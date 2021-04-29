package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
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
