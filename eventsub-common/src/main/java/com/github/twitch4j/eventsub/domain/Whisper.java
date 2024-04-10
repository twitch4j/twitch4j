package com.github.twitch4j.eventsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Whisper {

    /**
     * The body of the whisper message.
     */
    private String text;

}
