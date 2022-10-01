package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.MilliInstantDeserializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class UpdatedPinnedChatTiming {

    @JsonProperty("id")
    private String pinId;

    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant endsAt;

    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant updatedAt;

}
