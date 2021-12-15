package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Data
@Setter(AccessLevel.PRIVATE)
public class SoundtrackPlaylistTracksWrapper {

    /**
     * A list that contains the single playlist you requested.
     */
    @JsonProperty("data")
    private List<SoundtrackPlaylistTracks> playlists;

    /**
     * @return the single playlist you requested.
     */
    public Optional<SoundtrackPlaylistTracks> getPlaylist() {
        if (playlists == null || playlists.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(playlists.get(0));
    }

}
