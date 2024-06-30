package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.util.EnumUtil;
import lombok.Data;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

/**
 * @deprecated Friends are being removed by <a href="https://help.twitch.tv/s/article/how-to-use-the-friends-feature">Twitch</a> on 2022-05-25
 */
@Data
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class FriendshipData {
    private String userId;
    private String targetUserId;
    private Change change;

    /**
     * @deprecated Friends are being removed by <a href="https://help.twitch.tv/s/article/how-to-use-the-friends-feature">Twitch</a> on 2022-05-25
     */
    @Deprecated
    public enum Change {
        ACCEPTED,
        REJECTED,
        REMOVED,
        REQUESTED,
        SELF_ACCEPTED,
        SELF_REJECTED,
        SELF_REMOVED,
        SELF_REQUESTED;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }

        private static final Map<String, Change> MAPPINGS = EnumUtil.buildMapping(values());

        @Deprecated
        public static Change fromString(String change) {
            return MAPPINGS.get(change);
        }
    }
}
