package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class SoundtrackPlaylistMetadata {

    /**
     * The playlist’s title.
     */
    private String title;

    /**
     * The playlist’s ASIN (Amazon Standard Identification Number).
     */
    private String id;

    /**
     * A URL to the playlist’s image art.
     * Is empty if the playlist doesn't include art.
     */
    private String imageUrl;

    /**
     * A short description about the music that the playlist includes.
     */
    private String description;

}
