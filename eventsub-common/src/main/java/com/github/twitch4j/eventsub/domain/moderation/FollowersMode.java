package com.github.twitch4j.eventsub.domain.moderation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class FollowersMode {

    /**
     * The length of time, in minutes, that the followers must have followed the broadcaster to participate in the chat room.
     */
    private int followDurationMinutes;

}
