package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimData {
    @JsonIgnore
    private Instant timestamp;
    private Claim claim;

    @JsonProperty("timestamp")
    private void unpackTimestamp(final String timestamp) {
        this.timestamp = Instant.parse(timestamp);
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Claim {
        private String id;
        private String userId;
        private String channelId;
        private ChannelPointsGain pointGain;
        @JsonIgnore
        private Instant createdAt;

        @JsonProperty("created_at")
        private void unpackCreatedAt(final String createdAt) {
            this.createdAt = Instant.parse(createdAt);
        }
    }
}
