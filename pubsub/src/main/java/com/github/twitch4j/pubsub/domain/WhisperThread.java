package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class WhisperThread {
    @JsonProperty("id")
    private String threadId;
    private Long lastRead;
    private Boolean archived;
    private Boolean muted;
    private SpamClassification spamInfo;
    @JsonAlias("allowlisted_until")
    @JsonProperty("whitelisted_until")
    private Instant allowlistedUntil;

    public String getOtherUserId() {
        int delimIndex = threadId.indexOf('_');
        if (delimIndex < 0) return null;
        return threadId.substring(delimIndex + 1);
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class SpamClassification {
        private String likelihood; // e.g., "low"
        private Long lastMarkedNotSpam;
    }
}
