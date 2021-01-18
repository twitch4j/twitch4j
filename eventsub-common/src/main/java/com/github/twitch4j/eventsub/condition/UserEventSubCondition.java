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
public class UserEventSubCondition extends EventSubCondition {

    /**
     * The user ID for the user you want notifications for.
     */
    private String userId;

    @Override
    public Map<String, Object> toMap() {
        return Collections.singletonMap("user_id", this.userId);
    }

}
