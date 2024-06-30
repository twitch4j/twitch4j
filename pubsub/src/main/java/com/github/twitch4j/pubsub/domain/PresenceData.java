package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jetbrains.annotations.ApiStatus;

import java.time.Instant;
import java.util.List;

/**
 * @deprecated Friends are being removed by <a href="https://help.twitch.tv/s/article/how-to-use-the-friends-feature">Twitch</a> on 2022-05-25
 */
@Data
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
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

    /**
     * @deprecated Friends are being removed by <a href="https://help.twitch.tv/s/article/how-to-use-the-friends-feature">Twitch</a> on 2022-05-25
     */
    @Data
    @Deprecated
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
