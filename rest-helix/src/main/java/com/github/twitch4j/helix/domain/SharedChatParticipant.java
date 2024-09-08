package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

@Data
@Setter(AccessLevel.PRIVATE)
@ApiStatus.Experimental
public class SharedChatParticipant {

    /**
     * The User ID of the participant channel.
     */
    private String broadcasterId;

}
