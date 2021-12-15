package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class SoundtrackAlbum {

    /**
     * The album’s ASIN (Amazon Standard Identification Number).
     */
    private String id;

    /**
     * The album’s name.
     */
    private String name;

    /**
     * A URL to the album’s cover art.
     */
    private String imageUrl;

}
