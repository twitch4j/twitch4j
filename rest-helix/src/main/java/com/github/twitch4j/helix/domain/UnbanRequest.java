package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class UnbanRequest {

    /**
     * Unban request ID.
     */
    private String id;

    /**
     * User ID of broadcaster whose channel is receiving the unban request.
     */
    private String broadcasterId;

    /**
     * The broadcaster's login name.
     */
    private String broadcasterLogin;

    /**
     * The broadcaster's display name.
     */
    private String broadcasterName;

    /**
     * User ID of moderator who approved/denied the request.
     * <p>
     * Can be an empty string otherwise.
     */
    private String moderatorId;

    /**
     * User login name of moderator who approved/denied the request.
     * <p>
     * Can be an empty string otherwise.
     */
    private String moderatorLogin;

    /**
     * User display name of moderator who approved/denied the request.
     * <p>
     * Can be an empty string otherwise.
     */
    private String moderatorName;

    /**
     * User ID of the requester who is asking for an unban.
     */
    private String userId;

    /**
     * User login name of the requester who is asking for an unban.
     */
    private String userLogin;

    /**
     * User display name of the requester who is asking for an unban.
     */
    private String userName;

    /**
     * Text of the request from the requesting user.
     */
    private String text;

    /**
     * Status of the request.
     */
    private UnbanRequestStatus status;

    /**
     * Timestamp of when the unban request was created.
     */
    private Instant createdAt;

    /**
     * Timestamp of when moderator/broadcaster approved or denied the request.
     */
    @Nullable
    private Instant resolvedAt;

    /**
     * Text input by the resolver (moderator) of the unban request.
     */
    @Nullable
    private String resolutionText;

}
