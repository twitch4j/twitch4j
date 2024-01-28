package com.github.twitch4j.eventsub;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Conduit {

    /**
     * Conduit ID.
     */
    private String id;

    /**
     * Number of shards created for this conduit.
     */
    private int shardCount;

}
