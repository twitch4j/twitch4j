package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class HypeTrainConfig {
    private String channelId;
    private Boolean isEnabled;
    private Boolean isWhitelisted;
    private HypeTrainKickoff kickoff;
    private Long cooldownDuration;
    private Long levelDuration;
    private String difficulty;
    // private Object rewardEndDate;
    private HypeTrainParticipations participationConversionRates;
    private HypeTrainParticipations notificationThresholds;
    private DifficultySettings difficultySettings;
    private ConductorRewards conductorRewards;
    private String calloutEmoteId;
    private String calloutEmoteToken;
    private String themeColor;
    private Boolean hasConductorBadges;

    @Data
    public static class HypeTrainKickoff {
        private Integer numOfEvents;
        private Integer minPoints;
        private Long duration;
    }

    @Data
    public static class ConductorRewards {
        @JsonProperty("BITS")
        private ConductorReward bits;

        @JsonProperty("SUBS")
        private ConductorReward subs;

        @Data
        public static class ConductorReward {
            @JsonProperty("CURRENT")
            private List<RewardType> current;

            @JsonProperty("FORMER")
            private List<RewardType> former;

            @Data
            public static class RewardType {
                private String type;
                private String id;
                private String groupId;
                private Integer rewardLevel;
                private String badgeId;
                private String imageUrl;
            }
        }
    }

    @Data
    public static class DifficultySettings {
        @JsonProperty("EASY")
        private List<DifficultySetting> easy;

        @JsonProperty("MEDIUM")
        private List<DifficultySetting> medium;

        @JsonProperty("HARD")
        private List<DifficultySetting> hard;

        @JsonProperty("SUPER_HARD")
        private List<DifficultySetting> superHard;

        @JsonProperty("INSANE")
        private List<DifficultySetting> insane;

        @Data
        public static class DifficultySetting {
            private Integer value;
            private Integer goal;
            private List<HypeTrainReward> rewards;
        }
    }
}
