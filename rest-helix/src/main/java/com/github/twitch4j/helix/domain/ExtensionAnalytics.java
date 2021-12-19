package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * Extension Analytics
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ExtensionAnalytics {

    /**
     * ID of the extension whose analytics data is being provided.
     */
    @NonNull
    private String extensionId;

    /**
     * URL to the downloadable CSV file containing analytics data. Valid for 5 minutes.
     */
    @JsonProperty("URL")
    private String URL;

    /**
     * Type of report.
     */
    private String type;

    /**
     * Report contains data of this time range.
     */
    private AnaylticsDateRange dateRange;
}
