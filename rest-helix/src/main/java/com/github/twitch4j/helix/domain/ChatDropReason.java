package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChatDropReason {

    /**
     * Code for why the message was dropped.
     * <p>
     * For example: "channel_settings", "msg_duplicate", "msg_rejected", "user_warned".
     */
    private String code;

    /**
     * Message for why the message was dropped.
     */
    private String message;

}
