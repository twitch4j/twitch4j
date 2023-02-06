package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class InboundFollowers {

    /**
     * The list of users that follow the specified broadcaster,
     * in descending order by followed_at (with the most recent follower first).
     */
    @Nullable
    @JsonProperty("data")
    private List<InboundFollow> follows;

    /**
     * Contains the information used to page through the list of results.
     * <p>
     * The object is empty if there are no more pages left to page through.
     */
    private HelixPagination pagination;

    /**
     * The total number of users that follow this broadcaster.
     * <p>
     * As someone pages through the list, the number of users may change as users follow or unfollow the broadcaster.
     */
    private Integer total;

}
