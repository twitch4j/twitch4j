package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.pubsub.enums.PubSubType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * PubSub Request
 * <p>
 * Will ignore null values when serializing the request
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PubSubRequest {

    /**
     * Action Type
     */
    private PubSubType type;

    /**
     * Random string to identify the response associated with this request.
     */
    @EqualsAndHashCode.Exclude // topic matters more than nonce
    private String nonce;

    /**
     * Data (Body)
     */
    @EqualsAndHashCode.Exclude // covered by getTopics()
    private Map<String, Object> data = new HashMap<>(4);

    /**
     * Credential for {@link PubSubType#LISTEN} requests.
     */
    @JsonIgnore
    @ApiStatus.Internal
    @EqualsAndHashCode.Exclude // user_id in topic matters more than the specific credential
    private OAuth2Credential credential;

    @EqualsAndHashCode.Include // avoid duplicate LISTENs for same topic
    private Object getTopics() {
        return data.get("topics");
    }

}
