package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @deprecated <a href="https://discuss.dev.twitch.tv/t/withdrawal-of-twitch-api-endpoints-for-soundtrack/">Twitch is decommissioning Soundtrack on 2023-07-17</a>
 */
@Data
@Setter(AccessLevel.PRIVATE)
@Deprecated
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
