package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ModeratedChannelList {

    /**
     * The channels that the user has moderator privileges in.
     */
    @JsonProperty("data")
    private List<ModeratedChannel> channels;

    /**
     * Contains the information used to page through the list of results.
     * The object is empty if there are no more pages left to page through.
     */
    private HelixPagination pagination;

}
