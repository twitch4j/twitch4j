package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.NONE)
public class LowTrustUserNewMessage {

    @JsonProperty("low_trust_user")
    private LowTrustUser user;

    private AutomodCaughtMessage.Content messageContent;

    private String messageId;

    private Instant sentAt;

}
