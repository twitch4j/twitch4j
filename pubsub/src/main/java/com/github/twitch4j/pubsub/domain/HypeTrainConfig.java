package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
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
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HypeTrainKickoff {
        private Integer numOfEvents;
        private Integer minPoints;
        private Long duration;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConductorRewards {
        @JsonProperty("BITS")
        private ConductorReward bits;

        @JsonProperty("SUBS")
        private ConductorReward subs;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ConductorReward {
            @JsonProperty("CURRENT")
            private List<RewardType> current;

            @JsonProperty("FORMER")
            private List<RewardType> former;

            @Data
            @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
            @JsonIgnoreProperties(ignoreUnknown = true)
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
    @JsonIgnoreProperties(ignoreUnknown = true)
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
        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DifficultySetting {
            private Integer value;
            private Integer goal;
            private List<HypeTrainReward> rewards;
        }
    }
}
