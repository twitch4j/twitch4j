package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ChannelSearchResult {
    /**
     * Channel ID
     */
    @NonNull
    private String id;

    /**
     * Display name corresponding to the channel
     */
    @NonNull
    private String displayName;

    /**
     * Broadcaster Language field from the Channels service
     */
    private String broadcasterLanguage;

    /**
     * ID of the game being played
     */
    private String gameId;

    /**
     * Live status
     */
    private Boolean isLive;

    /**
     * Shows tag IDs that apply to the stream (live only)
     * <p>
     * Note: Category Tags are not returned
     *
     * @see <a href="https://www.twitch.tv/directory/all/tags">Tag types</a>
     */
    private List<String> tagsIds;

    /**
     * Thumbnail URL of the stream
     */
    private String thumbnailUrl;

    /**
     * Channel title
     */
    private String title;

    /**
     * Timestamp of when the stream started
     */
    private Instant startedAt;
}
