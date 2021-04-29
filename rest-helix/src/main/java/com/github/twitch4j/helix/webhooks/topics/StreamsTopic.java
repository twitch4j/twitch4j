package com.github.twitch4j.helix.webhooks.topics;

import com.github.twitch4j.helix.domain.StreamList;
import lombok.Getter;
import lombok.NonNull;

import java.util.TreeMap;

/**
 * Notifies when a stream changes; e.g., stream goes online or offline, the stream title changes, or the game changes.
 */
@Getter
public class StreamsTopic extends TwitchWebhookTopic<StreamList> {

    public static final String PATH = "/streams";

    private static TreeMap<String, Object> mapParameters(String userId) {
        TreeMap<String, Object> parameterMap = new TreeMap<>();
        parameterMap.put("user_id", userId);
        return parameterMap;
    }

    /**
     * @return The user whose stream is monitored.
     */
    private String channelId;

    /**
     * Notifies when a stream changes; e.g., stream goes online or offline, the stream title changes, or the game changes.
     *
     * @param userId Specifies the user whose stream is monitored.
     */
    public StreamsTopic(@NonNull String userId) {
        super(
            PATH,
            StreamList.class,
            mapParameters(userId)
        );
        this.channelId = userId;
    }

}
