package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

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
     * Login of the broadcaster.
     */
    private String broadcasterLogin;

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
     * Name of the game being played on the stream.
     */
    private String gameName;

    /**
     * Live status
     */
    private Boolean isLive;

    /**
     * Shows tag IDs that apply to the stream (live only)
     * <p>
     * Note: Category Tags are not returned
     *
     * @see <a href="https://discuss.dev.twitch.tv/t/adding-customizable-tags-to-the-twitch-api/42921">Deprecation Announcement</a>
     * @deprecated Twitch has deprecated tag ids in favor of {@link #getTags()} due to the latest custom tags system
     */
    @Nullable
    @Deprecated
    @JsonProperty("tag_ids")
    private List<String> tagsIds;

    /**
     * The tags applied to the channel.
     * <p>
     * Note: Unlike {@link #getTagsIds()}, these tags <i>are</i> returned for offline channels
     */
    private List<String> tags;

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
