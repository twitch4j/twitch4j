package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class UserAutomodCaughtMessage {

    /**
     * Identifier of the message.
     */
    private String messageId;

    /**
     * Current status of the message.
     */
    private AutomodCaughtMessageData.Status status;

}
