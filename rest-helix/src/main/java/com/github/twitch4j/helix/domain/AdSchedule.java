package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class AdSchedule {

    /**
     * The UTC timestamp of the broadcaster’s next scheduled ad, in RFC3339 format.
     * Empty if the channel has no ad scheduled or is not live.
     */
    @Nullable
    private Instant nextAdAt;

    /**
     * The UTC timestamp of the broadcaster’s last ad-break, in RFC3339 format.
     * Empty if the channel has not run an ad or is not live.
     */
    @Nullable
    private Instant lastAdAt;

    /**
     * The length in seconds of the scheduled upcoming ad break.
     */
    @JsonAlias({"duration", "duration_seconds"})
    private Integer lengthSeconds;

    /**
     * The amount of pre-roll free time remaining for the channel in seconds.
     * Returns 0 if they are currently not pre-roll free.
     */
    @JsonAlias("preroll_free_time")
    private Integer prerollFreeTimeSeconds;

    /**
     * The number of snoozes available for the broadcaster.
     */
    private Integer snoozeCount;

    /**
     * The UTC timestamp when the broadcaster will gain an additional snooze.
     */
    private Instant snoozeRefreshAt;

}
