package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.PollData;
import com.github.twitch4j.util.EnumUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class PollsEvent extends TwitchEvent {
    private final EventType type;
    private final PollData data;

    public PollsEvent(String type, PollData data) {
        this.type = EventType.MAPPINGS.get(type);
        this.data = data;
    }

    public enum EventType {
        POLL_CREATE,
        POLL_UPDATE,
        POLL_COMPLETE,
        POLL_ARCHIVE,
        POLL_TERMINATE;

        private static final Map<String, EventType> MAPPINGS = EnumUtil.buildMapping(values());
    }
}
