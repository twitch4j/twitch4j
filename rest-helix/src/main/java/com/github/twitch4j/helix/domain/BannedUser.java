package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class BannedUser {
    /**
     * User ID of a user who has been banned.
     */
    @NonNull
    private String userId;

    /**
     * Login of a user who has been banned.
     */
    private String userLogin;

    /**
     * Display name of a user who has been banned.
     */
    private String userName;

    /**
     * Timestamp for timeouts, empty for bans
     */
    private Instant expiresAt;
}
