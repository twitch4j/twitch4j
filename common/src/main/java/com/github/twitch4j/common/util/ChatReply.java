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
     * The msgId of the original message being replied to.
     */
    @NonNull
    String messageId;

    /**
     * The text of the original message being replied to.
     */
    String messageBody;

    /**
     * The id of the user who originally sent the message being replied to.
     */
    String userId;

    /**
     * The login name of the user who originally sent the message being replied to.
     */
    String userLogin;

    /**
     * The display name of the user who originally sent the message being replied to.
     */
    String displayName;

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
            unescapeTagValue(tags.get("reply-parent-display-name"))
        );
    }
}
