package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import org.jetbrains.annotations.ApiStatus;

/**
 * @deprecated Twitch no longer fires this unofficial event.
 */
@Data
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class FollowingData {
    private String displayName;
    private String username;
    private String userId;
}
