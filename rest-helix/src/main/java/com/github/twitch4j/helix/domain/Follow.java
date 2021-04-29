package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Follow
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Follow {

    /** ID of the user following the to_id user. */
    private String fromId;

    /** Login of the user following the to_id user. */
    private String fromLogin;

    /** Login name corresponding to from_id. */
    private String fromName;

    /** ID of the user being followed by the from_id user. */
    private String toId;

    /** Login of the user being followed by the from_id user. */
    private String toLogin;

    /** Login name corresponding to to_id. */
    private String toName;

    /** Date and time when the from_id user followed the to_id user. */
    @JsonProperty("followed_at")
    private Instant followedAtInstant;

    /**
     * @return the date and time when the from_id user followed the to_id user.
     * @deprecated in favor of getFollowedAtInstant
     */
    @JsonIgnore
    @Deprecated
    public LocalDateTime getFollowedAt() {
        return LocalDateTime.ofInstant(followedAtInstant, ZoneOffset.UTC);
    }

}
