package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class PresenceData {
    private String userId;
    private String userLogin;
    /**
     * User's availability. Examples include: "busy", "idle", "offline", "online"
     */
    private String availability;
    private Integer index;
    private Instant updatedAt;
    private Activity activity;
    private List<Activity> activities;

    @Data
    public static class Activity {
        /**
         * Activity Type. Examples include: "none", "watching", "broadcasting"
         */
        private String type;
        private String channelId;
        private String channelLogin;
        private String channelDisplayName;
        private String streamId;
        private String gameId;
        @JsonProperty("game")
        private String gameName;
    }
}
