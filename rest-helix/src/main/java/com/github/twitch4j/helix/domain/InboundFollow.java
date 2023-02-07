package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class InboundFollow {

    /**
     * An ID that uniquely identifies the user that’s following the broadcaster.
     */
    private String userId;

    /**
     * The user’s login name.
     */
    private String userLogin;

    /**
     * The user’s display name.
     */
    private String userName;

    /**
     * The UTC timestamp when the user started following the broadcaster.
     */
    private Instant followedAt;

}
