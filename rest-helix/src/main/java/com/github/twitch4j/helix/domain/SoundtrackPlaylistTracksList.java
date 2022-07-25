package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class SoundtrackPlaylistTracksList {

    /**
     * The list of tracks in the playlist.
     */
    @JsonProperty("data")
    private List<SoundtrackTrack> tracks;

    /**
     * Cursor for forward pagination; used to tell the server where to start fetching the next set of results, in a multi-page response.
     */
    private HelixPagination pagination;

}
