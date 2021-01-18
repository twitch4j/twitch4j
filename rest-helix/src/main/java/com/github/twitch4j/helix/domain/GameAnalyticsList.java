package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Game Analytics List
 */
@Data
public class GameAnalyticsList {
    /**
     * Data
     */
    @JsonProperty("data")
    private List<GameAnalytics> gameAnalytics;

    @JsonProperty("pagination")
    private HelixPagination pagination;

}
