package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class SharedChatParticipant {

    /**
     * The User ID of the participant channel.
     */
    @JsonAlias("broadcaster_user_id")
    private String broadcasterId;

}
