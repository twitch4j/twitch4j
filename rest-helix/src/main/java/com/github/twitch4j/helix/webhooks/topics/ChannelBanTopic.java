package com.github.twitch4j.helix.webhooks.topics;

import com.github.twitch4j.helix.domain.ModeratorEventList;
import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;
import java.util.TreeMap;

/**
 * Notifies when a broadcaster bans or un-bans people in their channel.
 */
@Getter
public class ChannelBanTopic extends TwitchWebhookTopic<ModeratorEventList> {

    public static final String PATH = "/moderation/banned/events";

    private static TreeMap<String, Object> mapParameters(String broadcasterId, String userId) {
        TreeMap<String, Object> parameterMap = new TreeMap<>();
        parameterMap.put("broadcaster_id", broadcasterId);
        parameterMap.put("first", 1);
        parameterMap.put("user_id", userId);
        return parameterMap;
    }

    /**
     * @return The ID of the channel for which to monitor ban events.
     */
    private String broadcasterId;

    /**
     * @return The user ID of the moderator added or removed.
     */
    private Optional<String> userId;

    /**
     * Notifies when a broadcaster bans or un-bans people in their channel.
     *
     * @param broadcasterId Required. The ID of the channel for which to monitor ban events.
     * @param userId Optional. Specifies the user ID of the moderator added or removed.
     */
    public ChannelBanTopic(@NonNull String broadcasterId, String userId) {
        super(
            PATH,
            ModeratorEventList.class,
            mapParameters(broadcasterId, userId)
        );
        this.broadcasterId = broadcasterId;
        this.userId = Optional.ofNullable(userId);
    }


}
