package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.CrowdChant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.Instant;

/**
 * @deprecated the crowd chant experiment was disabled by <a href="https://twitter.com/twitchsupport/status/1486036628523073539">Twitch</a> on 2022-02-02
 */
@Data
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Deprecated
public class CrowdChantCreatedEvent extends TwitchEvent {
    private Instant timestamp;
    private CrowdChant crowdChant;
}
