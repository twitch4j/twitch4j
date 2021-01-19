package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.util.TimeUtils;
import lombok.*;

import java.time.Instant;
import java.util.Calendar;

/**
 * Model representing a stream.
 * <p>
 * A stream is a channel, that is currently streaming live.
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class VideoMarker {

    /** Stream ID. */
    @NonNull
    private String id;

    /** RFC3339 timestamp of the marker. */
    @NonNull
    @JsonProperty("created_at")
    private Instant createdAtInstant;

    /** Description of the marker. */
    @NonNull
    private String description;

    /** Relative offset (in seconds) of the marker, from the beginning of the stream. */
    @NonNull
    private String position_seconds;

    /** A link to the stream with a query parameter that is a timestamp of the marker’s location. */
    @JsonProperty("URL")
    private String url;

    /**
     * @return the timestamp of the marker, in the system default zone
     * @deprecated in favor of getCreatedAtInstant()
     */
    @JsonIgnore
    @Deprecated
    public Calendar getCreatedAt() {
        return TimeUtils.fromInstant(createdAtInstant);
    }
}
