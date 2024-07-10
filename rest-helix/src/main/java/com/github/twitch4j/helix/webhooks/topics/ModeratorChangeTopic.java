package com.github.twitch4j.helix.webhooks.topics;

import com.github.twitch4j.helix.domain.ModeratorEventList;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.TreeMap;

/**
 * Notifies when a broadcaster adds or removes moderators.
 *
 * @deprecated Twitch decommissioned this API; please migrate to EventSub
 */
@Getter
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class ModeratorChangeTopic extends TwitchWebhookTopic<ModeratorEventList> {

    public static final String PATH = "/moderation/moderators/events";

    private static TreeMap<String, Object> mapParameters(String broadcasterId, String userId) {
        TreeMap<String, Object> parameterMap = new TreeMap<>();
        parameterMap.put("broadcaster_id", broadcasterId);
        parameterMap.put("first", 1);
        parameterMap.put("user_id", userId);
        return parameterMap;
    }

    /**
     * @return The user ID of the broadcaster.
     */
    private String broadcasterId;

    /**
     * @return The user ID of the moderator added or removed.
     */
    private Optional<String> userId;

    /**
     * Notifies when a broadcaster adds or removes moderators.
     *
     * @param broadcasterId Required. Specifies the user ID of the broadcaster.
     * @param userId Optional. Specifies the user ID of the moderator added or removed.
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/deprecation-of-websub-based-webhooks/32152">Will be decommissioned after 2021-09-16 in favor of EventSub</a>
     */
    @Deprecated
    public ModeratorChangeTopic(@NonNull String broadcasterId, String userId) {
        super(
            PATH,
            ModeratorEventList.class,
            mapParameters(broadcasterId, userId)
        );
        this.broadcasterId = broadcasterId;
        this.userId = Optional.ofNullable(userId);
    }

}
