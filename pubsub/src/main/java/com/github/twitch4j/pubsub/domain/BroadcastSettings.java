package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class BroadcastSettings {
    @JsonProperty("channel")
    private String channelLogin;
    private String channelId;

    @JsonProperty("old_status")
    private String oldTitle;
    @JsonProperty("status")
    private String title;

    private String oldGame;
    private String oldGameId; // "0" if unset

    private String game;
    private String gameId; // "0" if unset
}
