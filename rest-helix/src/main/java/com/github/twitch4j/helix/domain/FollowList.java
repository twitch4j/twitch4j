package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Follow List
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class FollowList {

    @JsonProperty("data")
    private List<Follow> follows;

    /**
     * Total number of items returned.
     * <p>
     * If only from_id was in the request, this is the total number of followed users.
     * If only to_id was in the request, this is the total number of followers.
     * If both from_id and to_id were in the request, this is 1 (if the "from" user follows the "to" user) or 0.
     */
    private Integer total;

    /**
     * A cursor value, to be used in a subsequent request to specify the starting point of the next set of results.
     */
    private HelixPagination pagination;

}
