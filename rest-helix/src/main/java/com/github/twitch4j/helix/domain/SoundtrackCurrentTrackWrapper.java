package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Data
@Setter(AccessLevel.PRIVATE)
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
