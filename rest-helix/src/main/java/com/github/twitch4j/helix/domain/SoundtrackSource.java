package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class SoundtrackSource {

    /**
     * The playlist’s or station’s ASIN (Amazon Standard Identification Number).
     */
    private String id;

    /**
     * The type of content that id maps to.
     */
    private Type contentType;

    /**
     * The playlist’s or station’s title.
     */
    public String title;

    /**
     * A URL to the playlist’s or station’s image art.
     */
    private String imageUrl;

    /**
     * A URL to the playlist on Soundtrack.
     * The string is empty if {@link #getContentType()} is {@link Type#STATION}.
     */
    private String soundtrackUrl;

    /**
     * A URL to the playlist on Spotify.
     * The string is empty if {@link #getContentType()} is {@link Type#STATION}.
     */
    private String spotifyUrl;

    public enum Type {
        PLAYLIST,
        STATION
    }

}
