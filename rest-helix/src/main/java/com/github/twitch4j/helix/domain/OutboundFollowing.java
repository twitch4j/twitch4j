package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class OutboundFollowing {

    /**
     * The list of broadcasters that the user follows,
     * in descending order by followed_at (with the most recently followed broadcaster first).
     */
    @Nullable
    @JsonProperty("data")
    private List<OutboundFollow> follows;

    /**
     * Contains the information used to page through the list of results.
     * <p>
     * The object is empty if there are no more pages left to page through.
     */
    private HelixPagination pagination;

    /**
     * The total number of broadcasters that the user follows.
     * <p>
     * As someone pages through the list, the number may change as the user follows or unfollows broadcasters.
     */
    private Integer total;

}
