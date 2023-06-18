package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.RadioData;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @deprecated <a href="https://discuss.dev.twitch.tv/t/withdrawal-of-twitch-api-endpoints-for-soundtrack/">Twitch is decommissioning Soundtrack on 2023-07-17</a>
 */
@Value
@EqualsAndHashCode(callSuper = false)
@Deprecated
public class RadioEvent extends TwitchEvent {
    RadioData data;
}
