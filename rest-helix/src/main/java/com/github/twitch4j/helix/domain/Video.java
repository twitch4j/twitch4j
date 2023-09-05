package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.util.EnumUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Video
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Video {

    /** ID of the video. */
    private String id;

    /** ID of the stream that the video originated from if the type is "archive". Otherwise set to null. */
    @Nullable
    private String streamId;

    /** ID of the user who owns the video. */
    private String userId;

    /** Login of the user who owns the video. */
    private String userLogin;

    /** Login name corresponding to user_id. */
    private String userName;

    /** Title of the video. */
    private String title;

    /** Description of the video. */
    private String description;

    /** Date when the video was created. */
    @JsonProperty("created_at")
    private Instant createdAtInstant;

    /** Date when the video was published. */
    @JsonProperty("published_at")
    private Instant publishedAtInstant;

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

    /** Array of muted segments in the video. If there are no muted segments, the value will be null. */
    @Nullable
    private List<MutedSegment> mutedSegments;

    /**
     * @return the {@link Type} of the video
     */
    public Type getParsedType() {
        return Type.MAPPINGS.get(this.type);
    }

    /**
     * Gets the thumbnail url for specific dimensions.
     *
     * @param width  Thumbnail width.
     * @param height Thumbnail height.
     * @return String
     */
    public String getThumbnailUrl(int width, int height) {
        // https://github.com/twitchdev/issues/issues/822
        if (thumbnailUrl.contains("_404/404_processing_")) {
            width = 320;
            height = 180;
        }

        return StringUtils.replaceEach(
            this.getThumbnailUrl(),
            new String[] { "%{width}", "%{height}" },
            new String[] { String.valueOf(width), String.valueOf(height) }
        );
    }

    /**
     * @return the timestamp when the video was created
     * @deprecated in favor of getCreatedAtInstant()
     */
    @JsonIgnore
    @Deprecated
    public Date getCreatedAt() {
        return Date.from(createdAtInstant);
    }

    /**
     * @return the timestamp when the video was published
     * @deprecated in favor of getPublishedAtInstant()
     */
    @JsonIgnore
    @Deprecated
    public Date getPublishedAt() {
        return Date.from(publishedAtInstant);
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class MutedSegment {
        /**
         * Duration of the muted segment.
         */
        Integer duration;

        /**
         * Offset in the video at which the muted segment begins.
         */
        Integer offset;
    }

    /**
     * The video's type.
     */
    public enum Type {

        /**
         * The default is “all,” which returns all video types.
         */
        @JsonEnumDefaultValue
        ALL,

        /**
         * On-demand videos (VODs) of past streams.
         */
        ARCHIVE,

        /**
         * Highlight reels of past streams.
         */
        HIGHLIGHT,

        /**
         * External videos that the broadcaster uploaded using the Video Producer.
         */
        UPLOAD;

        @ApiStatus.Internal
        public static final Map<String, Type> MAPPINGS = EnumUtil.buildMapping(values());

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    /**
     * The order to sort the returned videos in.
     */
    public enum SearchOrder {

        /**
         * Sort the results in descending order by when they were created (i.e., latest video first).
         */
        @JsonEnumDefaultValue
        TIME,

        /**
         * Sort the results in descending order by biggest gains in viewership (i.e., highest trending video first).
         */
        TRENDING,

        /**
         * Sort the results in descending order by most views (i.e., highest number of views first).
         */
        VIEWS;

        @ApiStatus.Internal
        public static final Map<String, SearchOrder> MAPPINGS = EnumUtil.buildMapping(values());

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    /**
     * A filter used to filter the list of videos by when they were published.
     */
    public enum SearchPeriod {
        @JsonEnumDefaultValue
        ALL,
        DAY,
        MONTH,
        WEEK;

        @ApiStatus.Internal
        public static final Map<String, SearchPeriod> MAPPINGS = EnumUtil.buildMapping(values());

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

}
