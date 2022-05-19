package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class SoundtrackArtist {

    /**
     * The ID of the Twitch user that created the track.
     * Is an empty string if a Twitch user didn't create the track.
     */
    private String creatorChannelId;

    /**
     * The artist’s ASIN (Amazon Standard Identification Number).
     */
    private String id;

    /**
     * The artist’s name.
     * This can be the band’s name or the solo artist’s name.
     */
    private String name;

}
