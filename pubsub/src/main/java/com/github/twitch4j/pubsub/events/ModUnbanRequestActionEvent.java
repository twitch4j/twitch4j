package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.ModeratorUnbanRequestAction;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ModUnbanRequestActionEvent extends TwitchEvent {
    String channelId;
    ModeratorUnbanRequestAction data;
}
