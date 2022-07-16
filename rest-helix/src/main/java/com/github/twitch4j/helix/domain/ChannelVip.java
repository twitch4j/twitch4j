package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChannelVip {

    /**
     * An ID that uniquely identifies the VIP user.
     */
    private String userId;

    /**
     * The user’s display name.
     */
    private String userName;

    /**
     * The user’s login name.
     */
    private String userLogin;

}
