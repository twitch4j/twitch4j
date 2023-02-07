package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class OutboundFollow {

    /**
     * An ID that uniquely identifies the broadcaster that this user is following.
     */
    private String broadcasterId;

    /**
     * The broadcaster’s login name.
     */
    private String broadcasterLogin;

    /**
     * The broadcaster’s display name.
     */
    private String broadcasterName;

    /**
     * The UTC timestamp when the user started following the broadcaster.
     */
    private Instant followedAt;

}
