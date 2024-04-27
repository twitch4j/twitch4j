package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.util.TimeUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Stream (LiveStream)
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Stream {

    /** Stream ID. */
    @NonNull
    private String id;

    /** ID of the user who is streaming. */
    @NonNull
    private String userId;

    /** Login of the user who is streaming. */
    @NonNull
    private String userLogin;

    /** Display name of the user who is streaming */
    @NonNull
    private String userName;

    /** ID of the game being played on the stream. */
    private String gameId;

    /** Name of the game being played. */
    private String gameName;

    /** Array of community IDs. */
    @Nullable
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    private List<UUID> communityIds;

    /** Stream type: "live" or "" (in case of error). */
    @NonNull
    private String type;

    /** Stream title. */
    @NonNull
    private String title;

    /** The tags applied to the stream. */
    private List<String> tags;

    /** Number of viewers watching the stream at the time of the query. */
    @NonNull
    private Integer viewerCount;

    /** UTC timestamp on when the stream started */
    @NonNull
    @JsonProperty("started_at")
    private Instant startedAtInstant;

    /**
     * Ids of active tags on the stream
     *
     * @see <a href="https://discuss.dev.twitch.tv/t/adding-customizable-tags-to-the-twitch-api/42921">Deprecation Announcement</a>
     * @deprecated Twitch has deprecated tag ids in favor of {@link #getTags()} due to the latest custom tags system
     */
    @Nullable
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    private List<UUID> tagIds;

    /** Indicates if the broadcaster has specified their channel contains mature content that may be inappropriate for younger audiences. */
    @Accessors(fluent = true)
    @JsonProperty("is_mature")
    private Boolean isMature;

    /** Stream language. */
    @NonNull
    private String language;

    /**
     * A template URL to an image of a frame from the last 5 minutes of the stream.
     * Contains "{width}" and "{height}" placeholders to obtain the size of the image you want, in pixels.
     * <p>
     * To obtain a populated URL, prefer calling {@link #getThumbnailUrl(int, int)} instead.
     */
    @NonNull
    @JsonProperty("thumbnail_url")
    private String thumbnailUrlTemplate;

    /**
     * Gets the stream uptime based on the start date.
     *
     * @return The stream uptime.
     */
    public Duration getUptime() {
        return Duration.between(startedAtInstant, Instant.now());
    }

    /**
     * Gets the thumbnail url for specific dimensions
     *
     * @param width  thumbnail width
     * @param height thumbnail height
     * @return populated thumbnail url for a specific resolution
     */
    public String getThumbnailUrl(int width, int height) {
        return thumbnailUrlTemplate.replace("{width}x{height}", String.format("%dx%d", width, height));
    }

    /**
     * @return the thumbnail url template
     * @deprecated in favor of {@link #getThumbnailUrlTemplate()} or {@link #getThumbnailUrl(int, int)}
     */
    @NonNull
    @JsonIgnore
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public String getThumbnailUrl() {
        return this.thumbnailUrlTemplate;
    }

    /**
     * @return the timestamp on when the stream started, in the system default zone
     * @deprecated in favor of getStartedAtInstant()
     */
    @JsonIgnore
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public Calendar getStartedAt() {
        return TimeUtils.fromInstant(startedAtInstant);
    }
}
