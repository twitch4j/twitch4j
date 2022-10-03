package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChattersList {

    /**
     * The list of users that are connected to the specified broadcaster’s chat room.
     */
    @JsonProperty("data")
    private List<Chatter> chatters;

    /**
     * Contains the information used to page through the list of results.
     */
    private HelixPagination pagination;

    /**
     * The total number of users that are connected to the broadcaster’s chat room.
     * <p>
     * As you page through the list, the number of users may change as users join and leave the chat room.
     */
    private Integer total;

}
