package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BannedUser {
    /**
     * User ID of a user who has been banned.
     */
    @NonNull
    private String userId;

    /**
     * Display name of a user who has been banned.
     */
    private String userName;

    /**
     * Timestamp for timeouts, empty for bans
     */
    private Instant expiresAt;
}
