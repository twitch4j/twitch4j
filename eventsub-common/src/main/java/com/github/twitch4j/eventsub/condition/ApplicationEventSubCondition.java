package com.github.twitch4j.eventsub.condition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Collections;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@Jacksonized
public class ApplicationEventSubCondition extends EventSubCondition {

    /**
     * Your application’s client id.
     * The provided client_id must match the client id in the application access token.
     */
    private String clientId;

    @Override
    public Map<String, Object> toMap() {
        return Collections.singletonMap("client_id", this.clientId);
    }

}
