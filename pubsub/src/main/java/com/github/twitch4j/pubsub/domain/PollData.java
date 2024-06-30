package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Setter(onMethod_ = { @Deprecated })
public class PollData {
    private String pollId;
    private String ownedBy;
    private String createdBy;
    private String title;
    private Instant startedAt;
    private Instant endedAt;
    private String endedBy;
    private Long durationSeconds;
    private PollSettings settings;
    private Status status;
    private List<PollChoice> choices;
    private Votes votes;
    private Tokens tokens;
    private Integer totalVoters;
    private Long remainingDurationMilliseconds;
    private Contributor topContributor;
    @Getter(onMethod_ = { @Deprecated })
    private Contributor topBitsContributor;
    private Contributor topChannelPointsContributor;

    @Data
    @Setter(onMethod_ = { @Deprecated })
    public static class PollSettings {
        private Setting multiChoice;
        private Setting subscriberOnly;
        private Setting subscriberMultiplier;
        @Getter(onMethod_ = { @Deprecated })
        private Setting bitsVotes;
        private Setting channelPointsVotes;

        @Data
        @Setter(onMethod_ = { @Deprecated })
        public static class Setting {
            private Boolean isEnabled;
            private Long cost;
        }
    }

    @Data
    @Setter(onMethod_ = { @Deprecated })
    public static class PollChoice {
        private String choiceId;
        private String title;
        private Votes votes;
        private Tokens tokens;
        private Integer totalVoters;
    }

    @Data
    @Setter(onMethod_ = { @Deprecated })
    public static class Votes {
        private Long total;
        @Getter(onMethod_ = { @Deprecated })
        private Long bits;
        private Long channelPoints;
        private Long base;
    }

    @Data
    @Setter(onMethod_ = { @Deprecated })
    public static class Tokens {
        @Getter(onMethod_ = { @Deprecated })
        private Long bits;
        private Long channelPoints;
    }

    @Data
    @Setter(onMethod_ = { @Deprecated })
    public static class Contributor {
        private String userId;
        private String displayName;
        @Getter(onMethod_ = { @Deprecated })
        private Long bitsContributed;
        private Long channelPointsContributed;
    }

    @SuppressWarnings("unused")
    public enum Status {
        ACTIVE,
        COMPLETED,
        ARCHIVED,
        TERMINATED,
        MODERATED,
        @JsonEnumDefaultValue
        INVALID
    }
}
