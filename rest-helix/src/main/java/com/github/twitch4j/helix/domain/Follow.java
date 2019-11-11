package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Follow
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Follow {

    /** ID of the user following the to_id user. */
    private String fromId;

    /** Login name corresponding to from_id. */
    private String fromName;

    /** ID of the user being followed by the from_id user. */
    private String toId;

    /** Login name corresponding to to_id. */
    private String toName;

    /** Date and time when the from_id user followed the to_id user. */
    private LocalDateTime followedAt;

}
