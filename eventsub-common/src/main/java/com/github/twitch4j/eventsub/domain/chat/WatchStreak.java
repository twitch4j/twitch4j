package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class WatchStreak {

    /**
     * The number of consecutive broadcasts for which the user has been watching.
     */
    private int streakCount;

    /**
     * The number of channel points awarded for the Watch Streak milestone.
     */
    private int channelPointsAwarded;

}
