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
    private @Deprecated String channelId;
    private @Deprecated Boolean isEnabled;
    private @Deprecated Boolean isWhitelisted;
    private @Deprecated HypeTrainKickoff kickoff;
    private @Deprecated Long cooldownDuration;
    private @Deprecated Long levelDuration;
    private String difficulty;
    @JsonProperty("participationConversionRates")
    private List<HypeTrainParticipation> conversionRates;
    @Deprecated
    @JsonIgnore
    private HypeTrainParticipations participationConversionRates;
    private @Deprecated HypeTrainParticipations notificationThresholds;
    @JsonProperty("difficultySettings")
    private List<DifficultySetting> difficultySetting;
    @Deprecated
    @JsonIgnore
    private DifficultySettings difficultySettings;
    @JsonProperty("conductorRewards")
    private List<HypeTrainConductorReward> leaderRewards;
    @Deprecated
    @JsonIgnore
    private ConductorRewards conductorRewards;
    private List<HypeTrainPotentialReward> potentialRewards;
    private Emote calloutEmote;
    private @Deprecated String calloutEmoteId;
    private @Deprecated String calloutEmoteToken;
    private @Deprecated String themeColor;
    @JsonProperty("willUseCreatorColor")
    private Boolean useCreatorColor;
    private @Nullable String primaryHexColor;
    private @Deprecated Boolean usePersonalizedSettings;
    private @Deprecated Boolean hasConductorBadges;

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
        @Setter(onMethod_ = { @Deprecated })
        public static class ConductorReward {
            @JsonProperty("CURRENT")
            private List<RewardType> current;

            @JsonProperty("FORMER")
            private List<RewardType> former;

            @Data
            @Setter(onMethod_ = { @Deprecated })
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
        @Setter(onMethod_ = { @Deprecated })
        public static class DifficultySetting {
            private Integer value;
            private Integer goal;
            private List<HypeTrainReward> rewards;
        }
    }
}
