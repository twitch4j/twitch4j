package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(value = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
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
