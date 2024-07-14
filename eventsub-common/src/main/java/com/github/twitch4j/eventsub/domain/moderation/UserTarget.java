package com.github.twitch4j.eventsub.domain.moderation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class UserTarget {

    /**
     * The ID of the user that is the target of the moderator action.
     */
    private String userId;

    /**
     * The login name of the user that is the target of the moderation action.
     */
    private String userLogin;

    /**
     * The display name of the user that is the target of the moderation action.
     */
    private String userName;

}
