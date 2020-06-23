package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.twitch4j.helix.TwitchHelix;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.util.List;

@Data
@With
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelInformation {
    /**
     * Twitch User ID of this channel owner
     */
    private String broadcasterId;

    /**
     * Twitch User Name of this channel owner
     * <p>
     * Note: Not adequately documented by Twitch, yet is available in {@link TwitchHelix#getChannelInformation(String, List)}.
     * Note: Appears to return the display_name instead of login
     */
    private String broadcasterName;

    /**
     * Language of the channel
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
