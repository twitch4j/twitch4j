package com.github.twitch4j.eventsub.domain.moderation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BanTarget extends UserTarget {

    /**
     * Optional reason given for the ban.
     */
    @Nullable
    private String reason;

}
