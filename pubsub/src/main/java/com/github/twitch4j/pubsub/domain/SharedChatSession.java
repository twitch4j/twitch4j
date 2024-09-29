package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class SharedChatSession {
    private String sessionId;
    private Status status;
    private String hostChannelId;
    private List<SharedChatParticipant> participants;
    private Instant createdAt;
    private Instant updatedAt;

    public enum Status {
        CREATED,
        ACTIVE,
        ENDED
    }
}
