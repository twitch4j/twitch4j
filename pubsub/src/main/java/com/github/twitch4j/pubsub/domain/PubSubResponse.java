package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.twitch4j.pubsub.enums.PubSubType;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PubSubResponse {

    /**
     * Action Type
     */
    private PubSubType type;

    /**
     * Random string to identify the response associated with this request.
     */
    private String nonce;

    /**
     * Error
     */
    private String error;

    /**
     * Payload
     */
    private PubSubResponsePayload data;

}
