package com.github.twitch4j.helix.eventsub.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
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
