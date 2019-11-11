package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.Calendar;

/**
 * Model representing a stream.
 * <p>
 * A stream is a channel, that is currently streaming live.
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoMarker {

    /** Stream ID. */
    @NonNull
    private String id;

    /** RFC3339 timestamp of the marker. */
    @NonNull
    private Calendar createdAt;

    /** Description of the marker. */
    @NonNull
    private String description;

    /** Relative offset (in seconds) of the marker, from the beginning of the stream. */
    @NonNull
    private String position_seconds;

    /** A link to the stream with a query parameter that is a timestamp of the marker’s location. */
    private String url;

}
