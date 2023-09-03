package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.DonationAmount;
import com.github.twitch4j.common.util.MilliInstantDeserializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class PinnedMessage {

    @JsonProperty("id")
    private String messageId; // corresponds to irc id

    private AutomodCaughtMessage.Sender sender;

    private Content content;

    private String type; // e.g., "MOD", "PAID"

    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant startsAt;

    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant updatedAt;

    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant endsAt;

    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant sentAt;

    @Nullable
    @JsonProperty("metadata")
    private DonationAmount hypeMetadata;

    public boolean isHypeChat() {
        return "PAID".equalsIgnoreCase(type);
    }

    public boolean isModPin() {
        return "MOD".equalsIgnoreCase(type);
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Content {
        private String text;
        private List<AutomodCaughtMessage.Fragment> fragments;
    }

}
