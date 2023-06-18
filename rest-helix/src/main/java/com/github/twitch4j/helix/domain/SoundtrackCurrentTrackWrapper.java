package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

/**
 * @deprecated <a href="https://discuss.dev.twitch.tv/t/withdrawal-of-twitch-api-endpoints-for-soundtrack/">Twitch is decommissioning Soundtrack on 2023-07-17</a>
 */
@Data
@Setter(AccessLevel.PRIVATE)
@Deprecated
public class SoundtrackCurrentTrackWrapper {

    /**
     * A list that contains the Soundtrack track that the broadcaster is playing.
     */
    private List<SoundtrackCurrentTrack> data;

    /**
     * @return the Soundtrack track that the broadcaster is playing.
     */
    public Optional<SoundtrackCurrentTrack> getTrack() {
        if (data == null || data.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(data.get(0));
    }

}
