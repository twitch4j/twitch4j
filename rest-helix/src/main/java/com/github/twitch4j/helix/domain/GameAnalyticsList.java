package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * Game Analytics List
 */
@Data
@Setter(onMethod_ = { @Deprecated })
public class GameAnalyticsList {
    /**
     * Data
     */
    @JsonProperty("data")
    private List<GameAnalytics> gameAnalytics;

    private HelixPagination pagination;

}
