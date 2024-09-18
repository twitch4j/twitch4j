package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class SharedChatSession {

    /**
     * The unique identifier for the shared chat session.
     */
    private String sessionId;

    /**
     * The User ID of the host channel.
     */
    private String hostBroadcasterId;

    /**
     * The list of participants in the session.
     */
    private List<SharedChatParticipant> participants;

    /**
     * The UTC date and time for when the session was created.
     */
    private Instant createdAt;

    /**
     * The UTC date and time for when the session was last updated.
     */
    private Instant updatedAt;

}
