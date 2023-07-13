package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Information about the available content classification labels.
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ContentClassificationList {

    /**
     * The list of CCLs available.
     */
    @JsonProperty("data")
    private List<ContentClassificationInfo> labels;

}
