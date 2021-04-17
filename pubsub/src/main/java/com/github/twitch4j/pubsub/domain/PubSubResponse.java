package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.pubsub.enums.PubSubType;
import lombok.Data;

@Data
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
