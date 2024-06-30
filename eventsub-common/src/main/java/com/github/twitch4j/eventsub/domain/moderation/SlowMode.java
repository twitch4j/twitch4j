package com.github.twitch4j.eventsub.domain.moderation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class SlowMode {

    /**
     * The amount of time, in seconds, that users need to wait between sending messages.
     */
    private int waitTimeSeconds;

}
