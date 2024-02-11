package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Cheer {

    /**
     * The amount of Bits the user cheered.
     */
    private int bits;

}
