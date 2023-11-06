package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Raid {

    /**
     * The user ID of the broadcaster raiding this channel.
     */
    private String userId;

    /**
     * The user name of the broadcaster raiding this channel.
     */
    private String userName;

    /**
     * The login name of the broadcaster raiding this channel.
     */
    private String userLogin;

    /**
     * Profile image URL of the broadcaster raiding this channel.
     */
    private String profileImageUrl;

    /**
     * The number of viewers raiding this channel from the broadcaster’s channel.
     */
    private Integer viewerCount;

}
