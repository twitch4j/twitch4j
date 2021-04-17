package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.twitch4j.pubsub.enums.PubSubType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * PubSub Request
 * <p>
 * Will ignore null values when serializing the request
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PubSubRequest {

    /**
     * Action Type
     */
    private PubSubType type;

    /**
     * Random string to identify the response associated with this request.
     */
    private String nonce;

    /**
     * Data (Body)
     */
    private Map<String, Object> data = new HashMap<>();

}
