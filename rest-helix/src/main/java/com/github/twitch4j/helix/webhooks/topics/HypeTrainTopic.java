package com.github.twitch4j.helix.webhooks.topics;

import com.github.twitch4j.helix.domain.HypeTrainEventList;
import lombok.Getter;
import lombok.NonNull;

import java.util.SortedMap;
import java.util.TreeMap;

public class HypeTrainTopic extends TwitchWebhookTopic<HypeTrainEventList> {
    public static final String PATH = "/hypetrain/events";

    @Getter
    private final String broadcasterId;

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
