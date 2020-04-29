package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

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
public class VideoMarkers {

    /** Stream ID. */
    @NonNull
    private String videoId;

    /** Markers */
    @NonNull
    private List<VideoMarker> markers;

}
