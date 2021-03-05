package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class TeamUser {

    /**
     * User ID of a Team member.
     */
    private String userId;

    /**
     * Display name of a Team member.
     */
    private String userName;

    /**
     * Login of a Team member.
     */
    private String userLogin;

}
