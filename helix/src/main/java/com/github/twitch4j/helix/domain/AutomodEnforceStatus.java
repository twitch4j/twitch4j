package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class AutomodEnforceStatus {
    /**
     * The msg_id passed in the body of the POST message. Maps each message to its status.
     */
    private String msgId;

    /**
     * Indicates if this message meets AutoMod requirements.
     */
    private Boolean isPermitted;
}
