package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class StreamMarkersList {

    @NonNull
    @JsonProperty("data")
    private List<StreamMarkers> streamMarkers;

    private HelixPagination pagination;

}
