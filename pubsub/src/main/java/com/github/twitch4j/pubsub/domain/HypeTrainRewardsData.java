package com.github.twitch4j.pubsub.domain;

import lombok.Data;

import java.util.List;

@Data
public class HypeTrainRewardsData {
    private String channelId;
    private Integer completedLevel;
    private List<Reward> rewards;

    @Data
    public static class Reward {
        private String type; // e.g. "EMOTE"
        private String id;
        private String groupId;
        private Integer rewardLevel;
    }
}
