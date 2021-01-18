package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Video List
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class VideoList {

    @JsonProperty("data")
    private List<Video> videos;

    private HelixPagination pagination;

}
