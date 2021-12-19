package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * User
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class User {

    /**
     * User’s ID.
     */
    private String id;

    /**
     * User’s login name.
     */
    private String login;

    /**
     * User’s display name.
     */
    private String displayName;

    /**
     * User’s type: "staff", "admin", "global_mod", or "".
     */
    private String type;

    /**
     * User’s broadcaster type: "partner", "affiliate", or "".
     */
    private String broadcasterType;

    /**
     * User’s channel description.
     */
    private String description;

    /**
     * URL of the user’s profile image.
     */
    private String profileImageUrl;

    /**
     * URL of the user’s offline image.
     */
    private String offlineImageUrl;

    /**
     * Total number of views of the user’s channel.
     */
    private Integer viewCount;

    /**
     * User’s email address. Returned if the request includes the user:read:email scope.
     */
    private String email;

    /**
     * Date when the user was created.
     */
    private Instant createdAt;

}
