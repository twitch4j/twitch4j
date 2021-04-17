package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.NanoInstantDeserializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class RadioData {

    @JsonProperty("userID")
    private String channelId;

    private String asin;

    private String type; // e.g. "RADIO_PLAYING"

    @JsonDeserialize(using = NanoInstantDeserializer.class)
    private Instant updatedAt;

    private RadioTrack track;

}
