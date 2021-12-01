package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class BanUsersResult {

    /**
     * The broadcaster whose chat room the user was banned from chatting in.
     */
    private String broadcasterId;

    /**
     * The moderator that banned or put the user in the timeout.
     */
    private String moderatorId;

    /**
     * The user that was banned or was put in a timeout.
     */
    private String userId;

    /**
     * The UTC date and time (in RFC3339 format) that the timeout will end.
     * Is null if the user was banned instead of put in a timeout.
     */
    @Nullable
    private Instant endTime;

}
