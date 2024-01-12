package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class ModeratedChannel {

    /**
     * An ID that uniquely identifies the channel this user can moderate.
     */
    private String broadcasterId;

    /**
     * The channel's login name.
     */
    private String broadcasterLogin;

    /**
     * The channel's display name.
     */
    private String broadcasterName;

}
