package com.github.twitch4j.pubsub.domain;

import lombok.Data;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @deprecated Friends are being removed by <a href="https://help.twitch.tv/s/article/how-to-use-the-friends-feature">Twitch</a> on 2022-05-25
 */
@Data
@Deprecated
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

        private static final Map<String, Change> MAPPINGS = Arrays.stream(values()).collect(Collectors.toMap(Enum::toString, Function.identity()));

        @Deprecated
        public static Change fromString(String change) {
            return MAPPINGS.get(change);
        }
    }
}
