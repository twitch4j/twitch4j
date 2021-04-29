package com.github.twitch4j.helix.domain;

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
public class StreamMarkers {

    /** Stream ID. */
    @NonNull
    private String userId;

    /** Display name corresponding to user_id. */
    @NonNull
    private String userName;

    /** Login corresponding to user_id. */
    @NonNull
    private String userLogin;

    /** Markers */
    @NonNull
    private List<VideoMarkers> videos;

}
