package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

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
public class StreamMetadata {

    /** ID of the user who is streaming. */
    @NonNull
    private String userId;

    /** ID of the game being played on the stream. */
    private String gameId;

    /** Object containing the Hearthstone metadata, if available; otherwise, null. */
    private HearthstoneMetadata hearthstone;

    /** Object containing the Overwatch metadata, if available; otherwise, null. */
    private OverwatchMetadata overwatch;
}
