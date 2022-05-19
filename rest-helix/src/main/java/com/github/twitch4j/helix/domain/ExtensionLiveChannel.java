package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class ExtensionLiveChannel {

    /**
     * User ID of the broadcaster.
     */
    private String broadcasterId;

    /**
     * Broadcaster’s display name.
     */
    private String broadcasterName;

    /**
     * Name of the game being played.
     */
    private String gameName;

    /**
     * ID of the game being played.
     */
    private String gameId;

    /**
     * Title of the stream.
     */
    private String title;

}
