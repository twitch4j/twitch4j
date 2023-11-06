package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Mention {

    /**
     * The user ID of the mentioned user.
     */
    private String userId;

    /**
     * The user name of the mentioned user.
     */
    private String userName;

    /**
     * The user login of the mentioned user.
     */
    private String userLogin;

}
