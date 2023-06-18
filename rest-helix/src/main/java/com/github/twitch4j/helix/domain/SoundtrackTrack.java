package com.github.twitch4j.helix.domain;

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
public class SoundtrackTrack {

    /**
     * The album that includes the track.
     */
    private SoundtrackAlbum album;

    /**
     * The artists included on the track.
     */
    private List<SoundtrackArtist> artists;

    /**
     * The duration of the track, in seconds.
     */
    private Integer duration;

    /**
     * The track’s ASIN (Amazon Standard Identification Number).
     */
    private String id;

    /**
     * The track’s ISRC (International Standard Recording Code).
     */
    @JsonProperty("isrc")
    private String code;

    /**
     * The track’s title.
     */
    private String title;

}
