package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class BitsBadge {

    /**
     * The tier of the Bits badge the user just earned. For example, 100, 1000, or 10000.
     */
    private String tier;

}
