package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class HypeTrainStatus {

    /**
     * An object describing the current Hype Train.
     * Null if a Hype Train is not active.
     */
    private @Nullable HypeTrain current;

    /**
     * An object with information about the channel’s Hype Train records.
     * Null if a Hype Train has not occurred.
     */
    private @Nullable HypeTrainMilestone allTimeHigh;

    /**
     * An object with information about the channel’s shared Hype Train records.
     * Null if a Hype Train has not occurred.
     */
    private @Nullable HypeTrainMilestone sharedAllTimeHigh;

}
