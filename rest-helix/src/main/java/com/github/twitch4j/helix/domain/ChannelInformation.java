package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Data
@With
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ChannelInformation {

    /**
     * Twitch User ID of this channel owner
     */
    private String broadcasterId;

    /**
     * Twitch User Display Name of this channel owner
     */
    private String broadcasterName;

    /**
     * Language of the channel
     * <p>
     * A language value is either the ISO 639-1 two-letter code for a supported stream language or “other”
     */
    private String broadcasterLanguage;

    /**
     * Current game ID being played on the channel
     */
    private String gameId;

    /**
     * Current game name being played on the channel
     */
    private String gameName;

    /**
     * Title of the stream
     */
    private String title;

}
