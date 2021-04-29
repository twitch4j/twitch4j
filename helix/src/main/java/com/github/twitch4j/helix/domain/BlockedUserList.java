package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class BlockedUserList {

    @JsonProperty("data")
    private List<BlockedUser> blocks;

    /**
     * Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response.
     */
    private HelixPagination pagination;

}
