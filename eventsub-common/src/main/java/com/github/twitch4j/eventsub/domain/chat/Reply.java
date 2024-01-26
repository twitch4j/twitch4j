package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Reply {

    /**
     * An ID that uniquely identifies the parent message that this message is replying to.
     */
    private String parentMessageId;

    /**
     * The message body of the parent message.
     */
    private String parentMessageBody;

    /**
     * User ID of the sender of the parent message.
     */
    private String parentUserId;

    /**
     * User display name of the sender of the parent message.
     */
    private String parentUserName;

    /**
     * User login name of the sender of the parent message.
     */
    private String parentUserLogin;

    /**
     * An ID that identifies the parent message of the reply thread.
     */
    private String threadMessageId;

    /**
     * User ID of the sender of the thread’s parent message.
     */
    private String threadUserId;

    /**
     * User display name of the sender of the thread’s parent message.
     */
    private String threadUserName;

    /**
     * User login name of the sender of the thread’s parent message.
     */
    private String threadUserLogin;

}
