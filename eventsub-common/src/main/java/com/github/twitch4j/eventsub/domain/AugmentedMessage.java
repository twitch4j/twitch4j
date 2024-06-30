package com.github.twitch4j.eventsub.domain;

import com.github.twitch4j.eventsub.domain.chat.Message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AugmentedMessage extends Message {

    /**
     * The UUID that identifies the message.
     */
    private String messageId;

}
