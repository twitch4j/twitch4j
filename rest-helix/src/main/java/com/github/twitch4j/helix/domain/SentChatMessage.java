package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class SentChatMessage {

    /**
     * The message id for the message that was sent.
     * <p>
     * Can be an empty string when {@link #isSent()} is false.
     */
    private String messageId;

    /**
     * Whether the message passed all checks and was sent.
     */
    @JsonProperty("is_sent")
    private boolean isSent;

    /**
     * The reason the message was dropped, if any.
     */
    @Nullable
    private ChatDropReason dropReason;

}
