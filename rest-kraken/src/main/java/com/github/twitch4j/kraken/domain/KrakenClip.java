package com.github.twitch4j.kraken.domain;

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
public class KrakenClip {
    private String slug;
    private String trackingId;
    private String url;
    private String embedUrl;
    private String embedHtml;
    private SimpleUser broadcaster;
    private SimpleUser curator;
    private VideoOnDemand vod;
    private String game;
    private String language;
    private String title;
    private Long views;
    private Double duration;
    private Instant createdAt;
    private Thumbnail thumbnails;

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class SimpleUser {
        private String id;
        private String name;
        private String displayName;
        private String channelUrl;
        private String logo;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class VideoOnDemand {
        private String id;
        private String url;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Thumbnail {
        private String medium;
        private String small;
        private String tiny;
    }
}
