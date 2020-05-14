package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.ChatModerationAction;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChatModerationEvent extends TwitchEvent {

    /**
     * Data regarding the moderation action that took place
     */
    private final ChatModerationAction data;

}
