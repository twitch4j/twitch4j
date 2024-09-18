package com.github.twitch4j.eventsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class SharedChatParticipant {

    /**
     * The User ID of the participant channel.
     */
    private String broadcasterUserId;

    /**
     * The display name of the participant channel.
     */
    private String broadcasterUserName;

    /**
     * The username of the participant channel.
     */
    private String broadcasterUserLogin;

}
