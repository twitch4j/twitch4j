package com.github.twitch4j.helix.domain;

import com.github.twitch4j.eventsub.domain.chat.Message;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@With
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = { @ApiStatus.Internal })
public class PinnedMessage {

    /**
     * The ID of the pinned chat message.
     */
    private String messageId;

    /**
     * The ID of the broadcaster.
     */
    private String broadcasterId;

    /**
     * The ID of the user who sent the pinned message.
     */
    private String senderUserId;

    /**
     * The login of the user who sent the pinned message.
     */
    private String senderUserLogin;

    /**
     * The display name of the user who sent the pinned message.
     */
    private String senderUserName;

    /**
     * The ID of the user who pinned the message.
     */
    private String pinnedByUserId;

    /**
     * The login of the user who pinned the message.
     */
    private String pinnedByUserLogin;

    /**
     * The display name of the user who pinned the message.
     */
    private String pinnedByUserName;

    /**
     * The pinned message content.
     */
    private Message message;

    /**
     * Timestamp of when the message was pinned.
     */
    private Instant startsAt;

    /**
     * The expiry timestamp. Null if pinned until stream ends.
     */
    @Nullable
    private Instant endsAt;

    /**
     * Timestamp of the last update to this pinned message.
     */
    private Instant updatedAt;

}
