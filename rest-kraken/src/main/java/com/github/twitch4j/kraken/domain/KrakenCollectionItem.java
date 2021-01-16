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
public class KrakenCollectionItem {

    @JsonProperty("_id")
    public String id;

    @JsonProperty("description_html")
    public String descriptionHtml;

    public Integer duration;
    public String game;

    @JsonProperty("item_id")
    public String itemId;

    @JsonProperty("item_type")
    public String itemType;
    public KrakenUser owner;

    @JsonProperty("published_at")
    public Instant publishedAt;

    public KrakenCollectionThumbnails thumbnails;
    public String title;
    public Integer views;
}
