package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * @deprecated <a href="https://discuss.dev.twitch.tv/t/withdrawal-of-twitch-api-endpoints-for-soundtrack/">Twitch is decommissioning Soundtrack on 2023-07-17</a>
 */
@Data
@Setter(AccessLevel.PRIVATE)
@Deprecated
public class SoundtrackPlaylistMetadataList {

    /**
     * The list of Soundtrack playlists.
     */
    @JsonProperty("data")
    private List<SoundtrackPlaylistMetadata> playlists;

    /**
     * Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     */
    @Unofficial
    private HelixPagination pagination;

}
