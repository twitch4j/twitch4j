package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
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
