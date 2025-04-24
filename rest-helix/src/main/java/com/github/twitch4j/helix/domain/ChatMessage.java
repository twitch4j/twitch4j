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

    /**
     * Whether the chat message is sent only to the source channel (broadcaster_id) during a shared chat session.
     * <p>
     * NOTE: Can only be set when utilizing an App Access Token at the top.
     * Cannot be specified when a User Access Token is used and will result in a 400 error.
     * <p>
     * Has no effect if the message is not sent during a shared chat session.
     * <p>
     * Introduced in April 2025, the default is temporarily “false”.
     * On May 19, 2025 the default value for the for_source_only parameter will be updated to “true”
     * (i.e. chat messages will be only shared with the source channel by default).
     * If you prefer to send a chat message to both channels in a shared chat session,
     * make sure this parameter is explicitly set to “false” in your API request before May 19.
     */
    @Nullable
    Boolean forSourceOnly;

    /**
     * Legacy Constructor
     *
     * @param broadcasterId        The ID of the broadcaster whose chat room the message will be sent to.
     * @param senderId             The ID of the user sending the message.
     * @param message              The message to send.
     * @param replyParentMessageId The ID of the chat message being replied to, if any.
     * @deprecated in favor of {@link ChatMessage#ChatMessage(String, String, String, String, Boolean)} or {@link ChatMessage#builder()}
     */
    @Deprecated
    public ChatMessage(@NotNull String broadcasterId, @NotNull String senderId, @NotNull String message, @Nullable String replyParentMessageId) {
        this.broadcasterId = broadcasterId;
        this.senderId = senderId;
        this.message = message;
        this.replyParentMessageId = replyParentMessageId;
    }

}
