package com.github.twitch4j.pubsub.domain;

import lombok.Data;

/**
 * @deprecated Twitch no longer fires this unofficial event.
 */
@Data
@Deprecated
public class FollowingData {
    private String displayName;
    private String username;
    private String userId;
}
