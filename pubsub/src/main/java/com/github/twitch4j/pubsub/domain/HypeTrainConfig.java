package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@Setter(onMethod_ = { @Deprecated })
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class HypeTrainConfig {
    private String id;
    private @Deprecated @Setter String channelId;
    private @Deprecated @Setter Boolean isEnabled;
    private @Deprecated @Setter Boolean isWhitelisted;
    private @Deprecated @Setter HypeTrainKickoff kickoff;
    private @Deprecated @Setter Long cooldownDuration;
    private @Deprecated @Setter Long levelDuration;
    private String difficulty;
    @JsonProperty("participationConversionRates")
    private List<HypeTrainParticipation> conversionRates;
    @Setter
    @Deprecated
    @JsonIgnore
    private HypeTrainParticipations participationConversionRates;
    private @Deprecated @Setter HypeTrainParticipations notificationThresholds;
    @JsonProperty("difficultySettings")
    private List<DifficultySetting> difficultySetting;
    @Setter
    @Deprecated
    @JsonIgnore
    private DifficultySettings difficultySettings;
    @JsonProperty("conductorRewards")
    private List<HypeTrainConductorReward> leaderRewards;
    @Setter
    @Deprecated
    @JsonIgnore
    private ConductorRewards conductorRewards;
    private List<HypeTrainPotentialReward> potentialRewards;
    private Emote calloutEmote;
    private @Deprecated @Setter String calloutEmoteId;
    private @Deprecated @Setter String calloutEmoteToken;
    private @Deprecated @Setter String themeColor;
    @JsonProperty("willUseCreatorColor")
    private Boolean useCreatorColor;
    private @Nullable String primaryHexColor;
    private @Deprecated @Setter Boolean usePersonalizedSettings;
    private @Deprecated @Setter Boolean hasConductorBadges;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
    public static class DifficultySetting {
        private String difficulty;
        private int maxLevel;
    }

    @Data
    @Deprecated
    public static class HypeTrainKickoff {
        private Integer numOfEvents;
        private Integer minPoints;
        private Long duration;
    }

    @Data
    @Deprecated
    public static class ConductorRewards {
        @JsonProperty("BITS")
        private ConductorReward bits;

        @JsonProperty("SUBS")
        private ConductorReward subs;

        @Data
        @Deprecated
        public static class ConductorReward {
            @JsonProperty("CURRENT")
            private List<RewardType> current;

            @JsonProperty("FORMER")
            private List<RewardType> former;

            @Data
            @Deprecated
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
    @Deprecated
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
        @Deprecated
        public static class DifficultySetting {
            private Integer value;
            private Integer goal;
            private List<HypeTrainReward> rewards;
        }
    }
}
