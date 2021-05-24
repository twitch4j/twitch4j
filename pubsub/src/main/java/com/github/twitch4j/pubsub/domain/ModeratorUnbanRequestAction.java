package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class ModeratorUnbanRequestAction {

    /**
     * Whether the unban request was approved or denied.
     */
    private Type moderationAction;

    /**
     * The user id of the moderator taking this action.
     */
    private String createdById;

    /**
     * The login name of the moderator taking this action.
     */
    private String createdByLogin;

    /**
     * An optional message provided to the target user by the moderator.
     */
    @Nullable
    private String moderatorMessage;

    /**
     * The id of the user that created the unban request.
     */
    private String targetUserId;

    /**
     * The login name of the user that created the unban request.
     */
    private String targetUserLogin;

    public enum Type {
        APPROVE_UNBAN_REQUEST,
        DENY_UNBAN_REQUEST
    }

}
