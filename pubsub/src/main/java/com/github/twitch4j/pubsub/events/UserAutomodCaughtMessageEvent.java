package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.UserAutomodCaughtMessage;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserAutomodCaughtMessageEvent extends TwitchEvent {
    String userId;
    String channelId;
    UserAutomodCaughtMessage data;
}
