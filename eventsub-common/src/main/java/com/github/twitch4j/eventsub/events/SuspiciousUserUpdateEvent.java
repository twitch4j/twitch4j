package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.SuspiciousStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SuspiciousUserUpdateEvent extends EventSubModerationEvent {

    /**
     * The status set for the suspicious user.
     */
    @JsonProperty("low_trust_status")
    private SuspiciousStatus status;

}
