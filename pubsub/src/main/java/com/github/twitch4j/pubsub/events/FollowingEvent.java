package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.FollowingData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @deprecated Twitch no longer fires this unofficial event.
 */
@Data
@Deprecated
@EqualsAndHashCode(callSuper = true)
public class FollowingEvent extends TwitchEvent {
    private final String channelId;
    private final FollowingData data;
}
