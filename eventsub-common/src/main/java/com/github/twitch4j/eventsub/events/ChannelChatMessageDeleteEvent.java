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
public class ChannelChatMessageDeleteEvent extends EventSubChannelEvent {

    /**
     * The ID of the user whose message was deleted.
     */
    private String targetUserId;

    /**
     * The user name of the user whose message was deleted.
     */
    private String targetUserName;

    /**
     * The user login of the user whose message was deleted.
     */
    private String targetUserLogin;

    /**
     * A UUID that identifies the message that was removed.
     */
    private String messageId;

}
