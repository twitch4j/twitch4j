package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Bits Leaderboard
 */
@Data
public class BitsLeaderboard {

    /**
     * Data
     */
    @JsonProperty("data")
    private List<BitsLeaderboardEntry> entries;

    @JsonProperty("pagination")
    private HelixPagination pagination;

}
