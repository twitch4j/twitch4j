package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Categories that matched the search query
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class CategorySearchList {

    @JsonProperty("data")
    private List<Game> results;

    private HelixPagination pagination;

}
