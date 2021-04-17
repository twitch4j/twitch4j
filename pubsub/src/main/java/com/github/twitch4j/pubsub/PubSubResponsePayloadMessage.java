package com.github.twitch4j.pubsub;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class PubSubResponsePayloadMessage {

    private String type;

    @JsonProperty("data")
    private JsonNode messageData;

    @JsonIgnore
    private String rawMessage;

}
