package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChatUserColor {

    /**
     * The ID of the user.
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

    /**
     * The Hex color code that the user uses in chat for their name.
     * <p>
     * If the user hasn't specified a color in their settings, the string is empty.
     */
    private String color;

}
