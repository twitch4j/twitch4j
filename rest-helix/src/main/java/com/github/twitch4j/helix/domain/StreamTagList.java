package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Stream Tags List
 */
@Data
public class StreamTagList {
    /**
     * Data
     */
    @JsonProperty("data")
    private List<StreamTag> streamTags;

    private HelixPagination pagination;

}
