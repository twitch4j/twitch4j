package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

/**
 * Analytics - Date Range
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class AnaylticsDateRange {

    /** Starting date/time for returned reports, in RFC3339 format with the hours, minutes, and seconds zeroed out and the UTC timezone: YYYY-MM-DDT00:00:00Z. */
    @JsonProperty("started_at")
    private Instant startedAtInstant;

    /** Ending date/time for returned reports, in RFC3339 format with the hours, minutes, and seconds zeroed out and the UTC timezone: YYYY-MM-DDT00:00:00Z. */
    @JsonProperty("ended_at")
    private Instant endedAtInstant;

    /**
     * @return the starting timestamp for returned reports
     * @deprecated in favor of getStartedAtInstant()
     */
    @JsonIgnore
    @Deprecated
    public Date getStartedAt() {
        return Date.from(startedAtInstant);
    }

    /**
     * @return the ending timestamp for returned reports
     * @deprecated in favor of getEndedAtInstant()
     */
    @JsonIgnore
    @Deprecated
    public Date getEndedAt() {
        return Date.from(endedAtInstant);
    }
}
