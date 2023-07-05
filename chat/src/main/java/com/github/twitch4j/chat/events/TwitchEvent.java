package com.github.twitch4j.chat.events;

import com.github.philippheuer.events4j.core.domain.Event;
import com.github.twitch4j.chat.TwitchChat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class TwitchEvent extends Event {

    /**
     * Constructor
     */
    public TwitchEvent() {
        super();
    }

    /**
     * Constructor
     *
     * @param eventId Unique event id
     * @param firedAt Timestamp of the event firing
     */
    public TwitchEvent(String eventId, Instant firedAt) {
        super(eventId, firedAt);
    }

    /**
     * Get TwitchChat
     *
     * @return TwitchChat Instance
     */
    public TwitchChat getTwitchChat() {
        return getServiceMediator().getService(TwitchChat.class, "twitch4j-chat");
    }
}
