package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.ShieldModeSettings;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ShieldModeSettingsUpdateEvent extends TwitchEvent {
    String userId;
    String channelId;
    ShieldModeSettings data;
}
