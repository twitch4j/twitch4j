package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChatMessage {

    /**
     * The ID of the broadcaster whose chat room the message will be sent to.
     */
    @NotNull
    String broadcasterId;

    /**
     * The ID of the user sending the message.
     * <p>
     * This ID must match the user ID in the user access token (if not using an app access token).
     */
    @NotNull
    String senderId;

    /**
     * The message to send.
     * <p>
     * The message is limited to a maximum of 500 characters.
     */
    @NotNull
    String message;

    /**
     * The ID of the chat message being replied to, if any.
     */
    @Nullable
    String replyParentMessageId;

}
