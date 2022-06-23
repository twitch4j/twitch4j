package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

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

    /**
     * The UTC date and time (in RFC3999 format) when the ban was created.
     */
    private Instant createdAt;

    /**
     * The reason for the ban if provided by the moderator.
     */
    @Nullable
    private String reason;

    /**
     * User ID of the moderator who initiated the ban.
     */
    private String moderatorId;

    /**
     * Login name of the moderator who initiated the ban.
     */
    private String moderatorLogin;

    /**
     * Display name of the moderator who initiated the ban.
     */
    private String moderatorName;
}
