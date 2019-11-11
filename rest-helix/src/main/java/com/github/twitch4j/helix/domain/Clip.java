package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Clip
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Clip {

    /** ID of the clip being queried. */
    private String id;

    /** URL where the clip can be viewed. */
    private String url;

    /** URL to embed the clip. */
    private String embedUrl;

    /** User ID of the stream from which the clip was created. */
    private String broadcasterId;

    /** ID of the user who created the clip. */
    private String creatorId;

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
    private Date createdAt;

    /** URL of the clip thumbnail. */
    private String thumbnailUrl;
}
