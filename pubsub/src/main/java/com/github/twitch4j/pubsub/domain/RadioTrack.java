package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * @deprecated <a href="https://discuss.dev.twitch.tv/t/withdrawal-of-twitch-api-endpoints-for-soundtrack/">Twitch is decommissioning Soundtrack on 2023-07-17</a>
 */
@Data
@Setter(AccessLevel.PRIVATE)
@Deprecated
public class RadioTrack {

    private String asin;

    private Integer duration; // in seconds

    private String title;

    private List<Artist> artists;

    private AlbumInfo album;

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Artist {
        private String asin;
        private String name;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class AlbumInfo {
        private String asin;
        private String name;
        @JsonProperty("imageURL")
        private String imageUrl;
    }

}
