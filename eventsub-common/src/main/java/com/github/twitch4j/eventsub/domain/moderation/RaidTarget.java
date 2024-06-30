package com.github.twitch4j.eventsub.domain.moderation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RaidTarget extends UserTarget {

    /**
     * The viewer count.
     */
    private int viewerCount;

}
