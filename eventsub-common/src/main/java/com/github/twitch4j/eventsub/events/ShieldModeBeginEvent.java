package com.github.twitch4j.eventsub.events;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.ApiStatus;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ShieldModeBeginEvent extends EventSubModeratorEvent {

    /**
     * The UTC timestamp of when the moderator activated Shield Mode.
     */
    private Instant startedAt;

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public String getUserId() {
        return getModeratorUserId();
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public String getUserName() {
        return getModeratorUserName();
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public String getUserLogin() {
        return getModeratorUserLogin();
    }

}
