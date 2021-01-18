package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

/**
 * Channels that matched the search query
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ChannelSearchList {

    @NonNull
    @JsonProperty("data")
    private List<ChannelSearchResult> results;

    private HelixPagination pagination;

}
