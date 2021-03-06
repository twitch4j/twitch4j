package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import lombok.Data;

@Data
public class PubSubResponsePayload {

    private String topic;

    @JsonIgnore
    private PubSubResponsePayloadMessage message;

    @SuppressWarnings("unchecked")
    @JsonProperty("message")
    private void unpackMessage(String message) {
        this.message = TypeConvert.jsonToObject(message, PubSubResponsePayloadMessage.class);
        this.message.setRawMessage(message);
    }
}
