package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Modiversary {

    /**
     * The number of months the user has been a moderator in this channel.
     */
    private int months;

}
