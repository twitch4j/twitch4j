package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class DateRange {

    /**
     * Start of the date range for the returned data.
     */
    private Instant startedAt;

    /**
     * End of the date range for the returned data.
     */
    private Instant endedAt;

}
