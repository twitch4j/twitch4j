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
 * Follow
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Video {

    /** ID of the video. */
    private String id;

    /** ID of the user who owns the video. */
    private String userId;

    /** Login name corresponding to user_id. */
    private String userName;

    /** Title of the video. */
    private String title;

    /** Description of the video. */
    private String description;

    /** Date when the video was created. */
    private Date createdAt;

    /** Date when the video was published. */
    private Date publishedAt;

    /** URL of the video. */
    private String url;

    /** Template URL for the thumbnail of the video. */
    private String thumbnailUrl;

    /** Indicates whether the video is publicly viewable. Valid values: "public", "private". */
    private String viewable;

    /** Number of times the video has been viewed. */
    private Integer viewCount;

    /** Language of the video. */
    private String language;

    /** Type of video. Valid values: "upload", "archive", "highlight". */
    private String type;

    /** Length of the video. */
    private String duration;

}
