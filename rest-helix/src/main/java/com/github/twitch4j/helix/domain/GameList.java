package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Game List
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class GameList {

    @JsonProperty("data")
    private List<Game> games;

    private HelixPagination pagination;

}
