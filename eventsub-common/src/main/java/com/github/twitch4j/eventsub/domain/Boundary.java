package com.github.twitch4j.eventsub.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Boundary {

    /**
     * Index in the message for the start of the problem (0 indexed, inclusive).
     */
    private int startPos;

    /**
     * Index in the message for the end of the problem (0 indexed, inclusive).
     */
    private int endPos;

}
