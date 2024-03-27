package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.ConduitShard;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ConduitShardList {

    /**
     * Information about a conduit's shards.
     */
    @JsonProperty("data")
    private List<ConduitShard> shards;

    /**
     * Contains information used to page through a list of results.
     * The object is empty if there are no more pages left to page through.
     */
    private HelixPagination pagination;

}
