package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class SharedChatParticipant {

    /**
     * The User ID of the participant channel.
     */
    private String broadcasterId;

}
