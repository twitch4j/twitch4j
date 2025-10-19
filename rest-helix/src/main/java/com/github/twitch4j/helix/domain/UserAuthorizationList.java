package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class UserAuthorizationList {

    /**
     * List of users and their authorized scopes.
     */
    private List<UserAuthorization> data;

}
