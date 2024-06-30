package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Bits Leaderboard
 */
@Data
@Setter(onMethod_ = { @Deprecated })
public class BitsLeaderboard {

    /**
     * Data
     */
    @JsonProperty("data")
    private List<BitsLeaderboardEntry> entries;

    /**
     * Date range of the returned data.
     */
    @Setter(AccessLevel.PRIVATE)
    private DateRange dateRange;

    /**
     * Total number of results (users) returned.
     * This is count or the total number of entries in the leaderboard, whichever is less.
     */
    @Setter(AccessLevel.PRIVATE)
    private Integer total;

    @Getter(onMethod_ = { @Deprecated })
    private HelixPagination pagination;

}
