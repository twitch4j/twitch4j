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
public abstract class ChannelChatUserEvent extends EventSubChannelEvent {

    /**
     * The user ID of the user that sent the message.
     */
    private String chatterUserId;

    /**
     * The user login of the user that sent the message.
     */
    private String chatterUserLogin;

    /**
     * The user name of the user that sent the message.
     */
    private String chatterUserName;

}
