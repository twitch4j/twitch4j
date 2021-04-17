package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

/**
 * Hype Train Events
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class HypeTrainEventList {

    @NonNull
    @JsonProperty("data")
    private List<HypeTrainEvent> events;

    private HelixPagination pagination;

}
