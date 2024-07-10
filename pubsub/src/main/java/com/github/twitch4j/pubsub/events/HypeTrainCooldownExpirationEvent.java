package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.ApiStatus;

/**
 * @deprecated Twitch no longer fires this event.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class HypeTrainCooldownExpirationEvent extends TwitchEvent {
    private final String channelId;
}
