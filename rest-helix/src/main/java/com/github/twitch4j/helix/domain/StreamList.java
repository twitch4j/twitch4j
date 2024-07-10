package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * Stream List
 */
@Data
@Setter(onMethod_ = { @Deprecated })
public class StreamList {
    /**
     * Data
     */
    @JsonProperty("data")
    private List<Stream> streams;

    private HelixPagination pagination;

}
