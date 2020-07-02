package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
public class AutomodEnforceCheck {
    /**
     * Developer-generated identifier for mapping messages to results.
     */
    String msgId;

    /**
     * Message text.
     */
    String msgText;

    /**
     * User ID of the sender.
     */
    String userId;

    /**
     * Constructs a message object to be checked against AutoMod enforcement settings
     *
     * @param message  The message to be checked
     * @param senderId The channel in which the message should be checked
     * @see com.github.twitch4j.helix.TwitchHelix#checkAutomodStatus(String, String, AutomodEnforceCheckList)
     */
    public AutomodEnforceCheck(String message, String senderId) {
        this.msgId = UUID.randomUUID().toString();
        this.msgText = message;
        this.userId = senderId;
    }
}
