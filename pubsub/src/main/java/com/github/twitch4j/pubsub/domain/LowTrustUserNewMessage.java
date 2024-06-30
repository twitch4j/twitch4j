package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class LowTrustUserNewMessage {

    /**
     * Information about the low trust message and suspicious user.
     */
    @JsonProperty("low_trust_user")
    private LowTrustUser user;

    /**
     * The chat message text and fragments sent by the suspicious user.
     */
    private AutomodCaughtMessage.Content messageContent;

    /**
     * ID of the chat message.
     */
    private String messageId;

    /**
     * Timestamp of when the chat message was sent.
     */
    private Instant sentAt;

}
