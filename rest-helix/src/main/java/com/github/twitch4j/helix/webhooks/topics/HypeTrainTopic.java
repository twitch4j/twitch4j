package com.github.twitch4j.helix.webhooks.topics;

import com.github.twitch4j.helix.domain.HypeTrainEventList;
import lombok.Getter;
import lombok.NonNull;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Notifies upon active hype train events
 */
public class HypeTrainTopic extends TwitchWebhookTopic<HypeTrainEventList> {
    public static final String PATH = "/hypetrain/events";

    /**
     * User ID of the broadcaster
     */
    @Getter
    private final String broadcasterId;

    /**
     * Notifies upon active hype train events
     *
     * @param broadcasterId User ID of the broadcaster
     */
    public HypeTrainTopic(@NonNull String broadcasterId) {
        super(PATH, HypeTrainEventList.class, mapParameters(broadcasterId));
        this.broadcasterId = broadcasterId;
    }

    private static SortedMap<String, Object> mapParameters(String broadcasterId) {
        SortedMap<String, Object> parameterMap = new TreeMap<>();
        parameterMap.put("first", 1);
        parameterMap.put("broadcaster_id", broadcasterId);
        return parameterMap;
    }
}
