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
public class ChannelEventSubCondition extends EventSubCondition {

    /**
     * The broadcaster user ID for the channel you want to get notifications for.
     */
    private String broadcasterUserId;

    @Override
    public Map<String, Object> toMap() {
        return Collections.singletonMap("broadcaster_user_id", this.broadcasterUserId);
    }

}
