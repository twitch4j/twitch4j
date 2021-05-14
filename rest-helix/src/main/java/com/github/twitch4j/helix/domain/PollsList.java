package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class PollsList {

    @JsonProperty("data")
    private List<Poll> polls;

    /**
     * Cursor for forward pagination: tells the server where to start fetching the next set of results in a multi-page response.
     */
    private HelixPagination pagination;

}
