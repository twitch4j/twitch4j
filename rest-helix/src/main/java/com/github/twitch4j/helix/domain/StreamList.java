package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Stream List
 */
@Data
public class StreamList {
    /**
     * Data
     */
    @JsonProperty("data")
    private List<Stream> streams;

    private HelixPagination pagination;

}
