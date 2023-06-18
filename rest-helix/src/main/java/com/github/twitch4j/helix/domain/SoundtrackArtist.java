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
