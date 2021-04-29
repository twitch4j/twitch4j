package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(value = AccessLevel.PRIVATE)
public class ChannelEditor {

    /**
     * User ID of the editor.
     */
    private String userId;

    /**
     * Display name of the editor.
     */
    private String userName;

    /**
     * Date and time the editor was given editor permissions.
     */
    private Instant createdAt;

}
