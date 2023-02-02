package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Chatter {

    /**
     * The ID of a user that’s connected to the broadcaster’s chat room.
     */
    private String userId;

    /**
     * The login name of a user that’s connected to the broadcaster’s chat room.
     */
    private String userLogin;

    /**
     * The display name of a user that’s connected to the broadcaster’s chat room.
     */
    private String userName;

}
