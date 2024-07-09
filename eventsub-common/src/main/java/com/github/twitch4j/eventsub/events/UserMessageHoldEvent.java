package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.chat.Message;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class UserMessageHoldEvent extends EventSubUserChannelEvent {

    /**
     * The ID of the message that was flagged by automod.
     */
    private String messageId;

    /**
     * The body of the message.
     */
    private Message message;

}
