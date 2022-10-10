package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.MilliInstantDeserializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class PinnedMessage {

    @JsonProperty("id")
    private String messageId; // corresponds to irc id

    private AutomodCaughtMessage.Sender sender;

    private Content content;

    private String type; // e.g., MOD

    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant startsAt;

    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant updatedAt;

    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant endsAt;

    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant sentAt;

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Content {
        private String text;
        private List<AutomodCaughtMessage.Fragment> fragments;
    }

}
