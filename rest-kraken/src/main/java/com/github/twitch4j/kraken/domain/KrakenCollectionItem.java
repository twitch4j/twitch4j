package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

/**
 * @deprecated Kraken is deprecated and has been shut down on <b>Febuary 28, 2022</b>.
 *             More details about the deprecation are available <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>.
 */
@Data
@Deprecated
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
