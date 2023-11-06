package com.github.twitch4j.eventsub.events;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelChatClearUserMessagesEvent extends EventSubChannelEvent {

    /**
     * The ID of the user that was banned or put in a timeout.
     * All of their messages are deleted.
     */
    private String targetUserId;

    /**
     * The user name of the user that was banned or put in a timeout.
     */
    private String targetUserName;

    /**
     * The user login of the user that was banned or put in a timeout.
     */
    private String targetUserLogin;

}
