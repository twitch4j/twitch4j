package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.twitch4j.pubsub.domain.ChannelPointsBalance;
import com.github.twitch4j.pubsub.domain.ChannelPointsGain;
import lombok.Data;

import java.time.Instant;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelPointsEarned {
    @JsonIgnore
    private Instant timestamp;
    private String channelId;
    private ChannelPointsGain pointGain;
    private ChannelPointsBalance balance;

    @JsonProperty("timestamp")
    private void unpackTimestamp(final String timestamp) {
        this.timestamp = Instant.parse(timestamp);
    }
}
