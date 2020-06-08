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
public class HypeTrainStart {
    private String channelId;
    private String id;
    private HypeTrainConfig config;
    private HypeTrainParticipations participations;
    private HypeTrainProgress progress;
    @JsonIgnore
    private Instant startedAt;
    @JsonIgnore
    private Instant expiresAt;
    @JsonIgnore
    private Instant updatedAt;
    // private Long ended_at;
    // private String ending_reason;
    // private Object conductors;

    @JsonProperty("started_at")
    private void unpackStartedAt(final Long startedAt) {
        if (startedAt != null)
            this.startedAt = Instant.ofEpochMilli(startedAt);
    }

    @JsonProperty("expires_at")
    private void unpackExpiresAt(final Long expiresAt) {
        if (expiresAt != null)
            this.expiresAt = Instant.ofEpochMilli(expiresAt);
    }

    @JsonProperty("updated_at")
    private void unpackUpdatedAt(final Long updatedAt) {
        if (updatedAt != null)
            this.updatedAt = Instant.ofEpochMilli(updatedAt);
    }
}
