package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(value = AccessLevel.PRIVATE)
public class BlockedUser {

    /**
     * User ID of the blocked user.
     */
    private String userId;

    /**
     * Login of the blocked user.
     */
    private String userLogin;

    /**
     * Display name of the blocked user.
     */
    private String displayName;

}
