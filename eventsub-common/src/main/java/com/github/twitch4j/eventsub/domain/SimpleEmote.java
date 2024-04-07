package com.github.twitch4j.eventsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class SimpleEmote {

    /**
     * The emote ID.
     */
    private String id;

    /**
     * The human-readable emote token.
     */
    private String name;

}
