package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class UserChatColorList {

    /**
     * The list of users and the color code that’s used for their name.
     */
    private List<ChatUserColor> data;

}
