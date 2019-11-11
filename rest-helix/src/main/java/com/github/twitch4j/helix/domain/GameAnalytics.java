package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

/**
 * Game Analytics
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameAnalytics {

    /** ID of the extension whose analytics data is being provided. */
    @NonNull
    private String gameId;

    /** URL to the downloadable CSV file containing analytics data. Valid for 5 minutes. */
    private String URL;

    /** Type of report. */
    private String type;

    /** Report contains data of this time range. */
    private AnaylticsDateRange dateRange;
}
