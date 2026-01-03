package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.auth.domain.TwitchScopes;
import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class UserAuthorization {

    /**
     * The user's ID.
     */
    private String userId;

    /**
     * The user's login name.
     */
    private String userLogin;

    /**
     * The user's display name.
     */
    private String userName;

    /**
     * An array of all the scopes the user has granted to the client ID.
     */
    private List<TwitchScopes> scopes;

    /**
     * Whether the user has authorized with the client ID.
     */
    @Unofficial // not officially documented
    @JsonProperty("has_authorized")
    private boolean authorized;

}
