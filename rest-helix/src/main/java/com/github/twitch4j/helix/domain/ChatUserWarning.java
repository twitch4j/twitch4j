package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChatUserWarning {

    /**
     * The ID of the channel in which the warning will take effect.
     */
    private String broadcasterId;

    /**
     * The ID of the warned user.
     */
    private String userId;

    /**
     * The ID of the user who applied the warning.
     */
    private String moderatorId;

    /**
     * The reason provided for warning.
     */
    private String reason;

}
