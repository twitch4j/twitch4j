package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventSubUserChannelEvent extends EventSubEvent {

    /**
     * The requested broadcaster ID.
     */
    private String broadcasterUserId;

    /**
     * The requested broadcaster name.
     */
    private String broadcasterUserName;

    /**
     * The user’s user id.
     */
    private String userId;

    /**
     * The user’s user name.
     */
    private String userName;

}
