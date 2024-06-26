package com.github.twitch4j.helix.webhooks.topics;

import com.github.twitch4j.helix.domain.FollowList;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.TreeMap;

/**
 * Notifies when a follows event occurs.
 *
 * @deprecated Twitch decommissioned this API; please migrate to EventSub
 */
@Getter
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class FollowsTopic extends TwitchWebhookTopic<FollowList> {

    public static final String PATH = "/users/follows";

    private static TreeMap<String, Object> mapParameters(String fromId, String toId) {
        TreeMap<String, Object> parameterMap = new TreeMap<>();
        parameterMap.put("first", 1);
        parameterMap.put("from_id", fromId);
        parameterMap.put("to_id", toId);
        return parameterMap;
    }

    /**
     * @return The user who starts following someone.
     */
    private Optional<String> fromId;

    /**
     * @return The user who has a new follower.
     */
    private Optional<String> toId;

    /**
     * Notifies when a follows event occurs.
     * At least one of fromId and toId is required.
     *
     * @param fromId Optional. Specifies the user who starts following someone.
     * @param toId Optional. Specifies the user who has a new follower.
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/deprecation-of-websub-based-webhooks/32152">Will be decommissioned after 2021-09-16 in favor of EventSub</a>
     */
    @Deprecated
    public FollowsTopic(String fromId, String toId) {
        super(
            PATH,
            FollowList.class,
            mapParameters(fromId, toId)
        );
        if(fromId == null && toId == null) throw new NullPointerException("At least one of fromId and toId is required.");
        this.fromId = Optional.ofNullable(fromId);
        this.toId = Optional.ofNullable(toId);
    }

}
