package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

/**
 * Clip
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Clip {

    /** ID of the clip being queried. */
    private String id;

    /** URL where the clip can be viewed. */
    private String url;

    /** URL to embed the clip. */
    private String embedUrl;

    /** User ID of the stream from which the clip was created. */
    private String broadcasterId;

    /** User display name of the stream from which the clip was created. */
    private String broadcasterName;

    /** Display name of the user who created the clip. */
    private String creatorId;

    /** Display name of the user who created the clip. */
    private String creatorName;

    /** ID of the video from which the clip was created. */
    private String videoId;

    /** ID of the game assigned to the stream when the clip was created. */
    private String gameId;

    /** Language of the stream from which the clip was created. */
    private String language;

    /** Title of the clip. */
    private String title;

    /** Number of times the clip has been viewed. */
    private Integer viewCount;

    /** Date when the clip was created. */
    @JsonProperty("created_at")
    private Instant createdAtInstant;

    /** URL of the clip thumbnail. */
    private String thumbnailUrl;

    /** Duration of the Clip in seconds (up to 0.1 precision). */
    private Float duration;

    /**
     * @return the timestamp for the clip's creation
     * @deprecated in favor of getCreatedAtInstant()
     */
    @JsonIgnore
    @Deprecated
    public Date getCreatedAt() {
        return Date.from(createdAtInstant);
    }
}
