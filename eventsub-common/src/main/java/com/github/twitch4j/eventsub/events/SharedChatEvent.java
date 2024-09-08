package com.github.twitch4j.eventsub.events;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class SharedChatEvent extends EventSubChannelEvent {

    /**
     * The unique identifier for the shared chat session.
     */
    private String sessionId;

    /**
     * The User ID of the host channel.
     */
    private String hostBroadcasterUserId;

    /**
     * The username of the host channel.
     */
    private String hostBroadcasterUserLogin;

    /**
     * The display name of the host channel.
     */
    private String hostBroadcasterUserName;

}
