package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Abstract base for result lists.
 */
@Data
public abstract class AbstractResultList {

    /**
     * Cursor
     */
    @JsonProperty("_cursor")
    private String cursor;

    /**
     * Total Entries
     */
    @JsonProperty("_total")
    private Long total;

}
