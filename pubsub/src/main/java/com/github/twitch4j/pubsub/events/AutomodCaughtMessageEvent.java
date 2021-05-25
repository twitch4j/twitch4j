package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.AutomodCaughtMessageData;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class AutomodCaughtMessageEvent extends TwitchEvent {
    String channelId;
    AutomodCaughtMessageData data;
}
