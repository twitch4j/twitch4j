package com.github.twitch4j.common.util;

import lombok.NonNull;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Objects;

import static com.github.twitch4j.common.util.EscapeUtils.unescapeTagValue;

/**
 * Meta-info regarding the <i>parent</i> message being replied to.
 */
@Value
public class ChatReply {

    public static final String REPLY_MSG_ID_TAG_NAME = "reply-parent-msg-id";

    /**
     * An ID that uniquely identifies the direct parent message that this message is replying to.
     */
    @NonNull
    String messageId;

    /**
     * The text of the direct parent message.
     */
    String messageBody;

    /**
     * An ID that identifies the sender of the direct parent message.
     */
    String userId;

    /**
     * The login name of the sender of the direct parent message.
     */
    String userLogin;

    /**
     * The display name of the sender of the direct parent message.
     */
    String displayName;

    /**
     * An ID that uniquely identifies the top-level parent message of the reply thread that this message is replying to.
     */
    String threadMessageId;

    /**
     * The login name of the sender of the top-level parent message.
     */
    String threadUserLogin;

    /**
     * The display name of the sender of the top-level parent message.
     */
    String threadUserName;

    /**
     * Attempts to parse a {@link ChatReply} instance from chat tags.
     *
     * @param tags the message tags associated with a reply.
     * @return the parsed {@link ChatReply}, or null if unsuccessful
     */
    @ApiStatus.Internal
    public static ChatReply parse(final Map<String, CharSequence> tags) {
        final CharSequence msgId;
        if (tags == null || (msgId = tags.get(REPLY_MSG_ID_TAG_NAME)) == null || msgId.length() == 0)
            return null;

        return new ChatReply(
            msgId.toString(),
            unescapeTagValue(tags.get("reply-parent-msg-body")),
            Objects.toString(tags.get("reply-parent-user-id"), null),
            Objects.toString(tags.get("reply-parent-user-login"), null),
            unescapeTagValue(tags.get("reply-parent-display-name")),
            Objects.toString(tags.get("reply-thread-parent-msg-id"), null),
            Objects.toString(tags.get("reply-thread-parent-user-login"), null),
            unescapeTagValue(tags.get("reply-thread-parent-display-name"))
        );
    }
}
