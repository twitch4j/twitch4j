package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Extension Analytics List
 */
@Data
public class ExtensionAnalyticsList {
    /**
     * Data
     */
    @JsonProperty("data")
    private List<ExtensionAnalytics> extensionAnalytics;

    private HelixPagination pagination;

}
