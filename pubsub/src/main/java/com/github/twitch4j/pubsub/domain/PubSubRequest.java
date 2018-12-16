package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
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
