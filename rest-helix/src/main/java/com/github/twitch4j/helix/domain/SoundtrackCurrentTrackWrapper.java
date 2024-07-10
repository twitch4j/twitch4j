package com.github.twitch4j.helix.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

/**
 * @deprecated <a href="https://discuss.dev.twitch.tv/t/withdrawal-of-twitch-api-endpoints-for-soundtrack/">Twitch is decommissioning Soundtrack on 2023-07-17</a>
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Deprecated
public class SoundtrackCurrentTrackWrapper extends ValueWrapper<SoundtrackCurrentTrack> {

    /**
     * @return the Soundtrack track that the broadcaster is playing.
     */
    public Optional<SoundtrackCurrentTrack> getTrack() {
        return Optional.ofNullable(this.get());
    }

}
