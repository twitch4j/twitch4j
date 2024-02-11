package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class UnbanRequestList {

    /**
     * Information about the channel's unban requests.
     */
    private List<UnbanRequest> data;

    /**
     * Contains information used to page through a list of results.
     * The object is empty if there are no more pages left to page through.
     */
    private HelixPagination pagination;

}
