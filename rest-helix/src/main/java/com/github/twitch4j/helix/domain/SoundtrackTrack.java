package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
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
     * The track’s title.
     */
    private String title;

}
