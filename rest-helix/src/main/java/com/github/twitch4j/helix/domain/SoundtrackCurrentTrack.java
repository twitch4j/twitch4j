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
public class SoundtrackCurrentTrack {

    /**
     * The track that’s currently playing.
     */
    private SoundtrackTrack track;

    /**
     * The source of the track that’s currently playing.
     * For example, a playlist or station.
     */
    private SoundtrackSource source;

}
