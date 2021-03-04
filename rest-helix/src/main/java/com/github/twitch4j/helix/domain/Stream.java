package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.util.TimeUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

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
    @Deprecated
    private List<UUID> communityIds;

    /** Stream type: "live" or "" (in case of error). */
    @NonNull
    private String type;

    /** Stream title. */
    @NonNull
    private String title;

    /** Number of viewers watching the stream at the time of the query. */
    @NonNull
    private Integer viewerCount;

    /** UTC timestamp on when the stream started */
    @NonNull
    @JsonProperty("started_at")
    private Instant startedAtInstant;

    /** Ids of active tags on the stream */
    private List<UUID> tagIds = new ArrayList<>();

    /** Stream language. */
    @NonNull
    private String language;

    /** Thumbnail URL of the stream. All image URLs have variable width and height. You can replace {width} and {height} with any values to get that size image */
    @NonNull
    private String thumbnailUrl;

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
     * @return String
     */
    public String getThumbnailUrl(Integer width, Integer height) {
        return thumbnailUrl.replaceAll(Pattern.quote("{width}"), width.toString()).replaceAll(Pattern.quote("{height}"), height.toString());
    }

    /**
     * @return the timestamp on when the stream started, in the system default zone
     * @deprecated in favor of getStartedAtInstant()
     */
    @JsonIgnore
    @Deprecated
    public Calendar getStartedAt() {
        return TimeUtils.fromInstant(startedAtInstant);
    }
}
