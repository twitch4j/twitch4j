package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Extension List
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ExtensionList {

    @JsonProperty("data")
    private List<Extension> extensions;

    @Deprecated
    private HelixPagination pagination;

}
