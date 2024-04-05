package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class UnbanRequestCreatedEvent extends EventSubUserChannelEvent {

    /**
     * The ID of the unban request.
     */
    @JsonProperty("id")
    private String requestId;

    /**
     * Message sent in the unban request.
     */
    private String text;

    /**
     * The UTC timestamp (in RFC3339 format) of when the unban request was created.
     */
    private Instant createdAt;

}
